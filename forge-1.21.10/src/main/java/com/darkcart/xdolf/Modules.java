package com.darkcart.xdolf;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import java.util.List;
import java.util.ArrayList;

final class Modules {
    static List<ClientModule> create() {
        var modules = new ArrayList<ClientModule>(List.of(
            new ClientModule("Sprint", "Sprint while moving forward and able to sprint.", "Player") {
                private LocalPlayer owner;
                private boolean applied;
                public void tick(Minecraft mc) {
                    var player = mc.player;
                    boolean eligible = mc.options.keyUp.isDown() && !player.isShiftKeyDown()
                        && !player.horizontalCollision && !player.isUsingItem()
                        && (player.getFoodData().getFoodLevel() > 6 || player.getAbilities().mayfly);
                    if (eligible && !player.isSprinting()) {
                        owner = player;
                        applied = true;
                        player.setSprinting(true);
                    } else if (!eligible) reset(mc);
                }
                public void reset(Minecraft mc) {
                    if (applied && owner != null) owner.setSprinting(false);
                    applied = false;
                    owner = null;
                }
            },
            new ClientModule("AutoWalk", "Hold forward until disabled or a screen opens.", "Player") {
                private boolean applied;
                public void tick(Minecraft mc) {
                    if (!mc.options.keyUp.isDown()) {
                        mc.options.keyUp.setDown(true);
                        applied = true;
                    }
                }
                public void reset(Minecraft mc) {
                    if (applied) mc.options.keyUp.setDown(false);
                    applied = false;
                }
            },
            new ClientModule("AutoRespawn", "Request respawn once after each death.", "Player") {
                private LocalPlayer lastDeath;
                public void tick(Minecraft mc) {
                    if (mc.player.isDeadOrDying()) {
                        if (lastDeath != mc.player) {
                            lastDeath = mc.player;
                            mc.player.respawn();
                        }
                    } else lastDeath = null;
                }
                public void reset(Minecraft mc) { lastDeath = null; }
            },
            new ClientModule("AutoLog", "Disconnect at 6 health (3 hearts) or below.", "Combat") {
                public void tick(Minecraft mc) {
                    if (mc.player.isAlive() && mc.player.getHealth() <= 6) {
                        setEnabled(false);
                        mc.getConnection().getConnection().disconnect(Component.literal("Xdolf AutoLog: low health"));
                    }
                }
            },
            new ClientModule("CrystalLog", "Disconnect when an end crystal is within 6 blocks.", "Combat") {
                public void tick(Minecraft mc) {
                    if (!mc.level.getEntitiesOfClass(EndCrystal.class, mc.player.getBoundingBox().inflate(6),
                        crystal -> crystal.distanceToSqr(mc.player) <= 36).isEmpty()) {
                        setEnabled(false);
                        mc.getConnection().getConnection().disconnect(Component.literal("Xdolf CrystalLog: nearby crystal"));
                    }
                }
            }
        ));
        MovementModules.addTo(modules);
        CombatModules.addTo(modules);
        InventoryModules.addTo(modules);
        modules.add(new AutoFishModule());
        HookModules.addTo(modules);
        RenderOverlays.addTo(modules);
        NetworkModules.addTo(modules);
        modules.add(new FreecamModule());
        return List.copyOf(modules);
    }
}
