package com.darkcart.xdolf;

import com.darkcart.xdolf.mixin.GameRendererAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
import java.util.ArrayList;
import java.util.List;

final class RenderOverlays {
    private static List<BlockPos> storage = List.of();
    private static List<Vec3> trajectory = List.of();

    static void addTo(List<ClientModule> modules) {
        modules.add(new ClientModule("Tracers", "Lines to nearby living entities, through walls.", "Render") {
            { setting("range", 64, 8, 128, 8); }
            public void tick(Minecraft mc) {}
        });
        modules.add(new ClientModule("Nametags", "Names, health and distance for nearby living entities.", "Render") {
            { setting("range", 64, 8, 128, 8); }
            public void tick(Minecraft mc) {}
        });
        modules.add(new ClientModule("StorageESP", "Boxes around storage in nearby loaded chunks.", "Render") {
            int delay;
            public void tick(Minecraft mc) {
                if (delay-- > 0) return;
                delay = 20;
                var list = new ArrayList<BlockPos>();
                int cx = mc.player.blockPosition().getX() >> 4, cz = mc.player.blockPosition().getZ() >> 4;
                for (int x = cx - 2; x <= cx + 2; x++) for (int z = cz - 2; z <= cz + 2; z++) {
                    var chunk = mc.level.getChunkSource().getChunk(x, z, ChunkStatus.FULL, false);
                    if (chunk instanceof LevelChunk loaded) for (var entity : loaded.getBlockEntities().values()) {
                        if ((entity instanceof BaseContainerBlockEntity || entity instanceof EnderChestBlockEntity)
                            && entity.getBlockPos().distToCenterSqr(mc.player.position()) < 64 * 64) list.add(entity.getBlockPos().immutable());
                    }
                }
                list.sort(java.util.Comparator.comparingDouble(pos -> pos.distToCenterSqr(mc.player.position())));
                storage = List.copyOf(list.subList(0, Math.min(list.size(), 64)));
            }
            public void reset(Minecraft mc) { storage = List.of(); delay = 0; }
        });
        modules.add(new ClientModule("Trajectories", "Preview a projectile path and its first block collision.", "Render") {
            public void tick(Minecraft mc) {
                var item = mc.player.getMainHandItem();
                double speed, gravity;
                if (item.is(Items.BOW)) {
                    double charge = Math.min(1, mc.player.getTicksUsingItem() / 20d);
                    speed = mc.player.isUsingItem() ? Math.min(1, (charge * charge + 2 * charge) / 3) * 3 : 3;
                    gravity = 0.05;
                } else if (item.is(Items.CROSSBOW)) { speed = 3.15; gravity = 0.05; }
                else if (item.is(Items.TRIDENT)) { speed = 2.5; gravity = 0.05; }
                else if (item.is(Items.SNOWBALL) || item.is(Items.EGG) || item.is(Items.ENDER_PEARL)) { speed = 1.5; gravity = 0.03; }
                else { trajectory = List.of(); return; }
                var position = mc.player.getEyePosition().add(0, -0.1, 0);
                var motion = mc.player.getLookAngle().scale(speed);
                var points = new ArrayList<Vec3>(); points.add(position);
                for (int i = 0; i < 100; i++) {
                    var next = position.add(motion);
                    var hit = mc.level.clip(new ClipContext(position, next, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, mc.player));
                    if (hit.getType() != HitResult.Type.MISS) { points.add(hit.getLocation()); break; }
                    points.add(next); position = next; motion = motion.scale(0.99).add(0, -gravity, 0);
                }
                trajectory = List.copyOf(points);
            }
            public void reset(Minecraft mc) { trajectory = List.of(); }
        });
    }

    private record Point(int x, int y) {}
    private record Projection(Vec3 origin, Quaternionf rotation, Matrix4f matrix, int width, int height) {
        Point project(Vec3 world) {
            var relative = world.subtract(origin);
            var view = new Vector3f((float) relative.x, (float) relative.y, (float) relative.z).rotate(rotation);
            var clip = new Vector4f(view.x, view.y, view.z, 1).mul(matrix);
            if (clip.w <= 0.05) return null;
            float x = clip.x / clip.w, y = clip.y / clip.w;
            if (Math.abs(x) > 1.2 || Math.abs(y) > 1.2) return null;
            return new Point((int) ((x + 1) * width / 2), (int) ((1 - y) * height / 2));
        }
    }

    static void render(GuiGraphics graphics, DeltaTracker delta) {
        var mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null || mc.screen != null || mc.options.hideGui) return;
        boolean tracers = Hooks.enabled("Tracers"), names = Hooks.enabled("Nametags");
        boolean boxes = Hooks.enabled("StorageESP"), paths = Hooks.enabled("Trajectories");
        if (!tracers && !names && !boxes && !paths) return;
        var camera = mc.gameRenderer.getMainCamera();
        float partial = delta.getGameTimeDeltaPartialTick(false);
        float fov = ((GameRendererAccess) mc.gameRenderer).xdolf$fov(camera, partial, true);
        var projection = new Projection(camera.getPosition(), new Quaternionf(camera.rotation()).conjugate(),
            mc.gameRenderer.getProjectionMatrix(fov), mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight());
        int count = 0;
        for (var entity : mc.level.entitiesForRendering()) {
            if ((!tracers && !names) || !(entity instanceof LivingEntity living) || !Hooks.highlight(entity)) continue;
            if (++count > 48) break;
            var point = projection.project(entity.getPosition(partial).add(0, entity.getBbHeight() + 0.2, 0));
            if (point == null) continue;
            double distance = entity.distanceTo(mc.player);
            int color = SocialState.isFriend(entity.getName().getString()) ? 0xFF77EDAA : 0xFF79CFFF;
            if (tracers && distance <= Hooks.setting("Tracers", "range", 64))
                line(graphics, new Point(projection.width / 2, projection.height / 2), point, color);
            if (names && distance <= Hooks.setting("Nametags", "range", 64)) {
                String label = entity.getName().getString() + " | " + (int) Math.ceil(living.getHealth()) + " HP | " + (int) distance + "m";
                int width = mc.font.width(label);
                graphics.fill(point.x - width / 2 - 3, point.y - 2, point.x + width / 2 + 3, point.y + 10, 0xB0101620);
                graphics.drawString(mc.font, label, point.x - width / 2, point.y, color);
            }
        }
        if (boxes) for (var pos : storage) box(graphics, projection, new AABB(pos), 0xFFFFCD72);
        if (paths) {
            Point previous = null;
            for (var position : trajectory) {
                var point = projection.project(position);
                if (point != null && previous != null) line(graphics, previous, point, 0xFF7BEEBD);
                previous = point;
            }
            if (previous != null) graphics.fill(previous.x - 3, previous.y - 3, previous.x + 3, previous.y + 3, 0xFF7BEEBD);
        }
    }

    private static void box(GuiGraphics graphics, Projection projection, AABB box, int color) {
        Point[] corners = new Point[8];
        for (int i = 0; i < 8; i++) corners[i] = projection.project(new Vec3((i & 1) == 0 ? box.minX : box.maxX,
            (i & 2) == 0 ? box.minY : box.maxY, (i & 4) == 0 ? box.minZ : box.maxZ));
        for (int i = 0; i < 8; i++) for (int mask : new int[] {1, 2, 4}) {
            int other = i ^ mask;
            if (i < other && corners[i] != null && corners[other] != null) line(graphics, corners[i], corners[other], color);
        }
    }

    private static void line(GuiGraphics graphics, Point a, Point b, int color) {
        int steps = Math.min(512, Math.max(Math.abs(b.x - a.x), Math.abs(b.y - a.y)));
        if (steps == 0) return;
        for (int i = 0; i <= steps; i += 2) {
            int x = a.x + (b.x - a.x) * i / steps, y = a.y + (b.y - a.y) * i / steps;
            graphics.fill(x, y, x + 2, y + 2, color);
        }
    }
}
