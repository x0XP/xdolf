package com.darkcart.xdolf;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;

/** Immutable block selection and a volatile flag are safe to read on chunk workers. */
public final class XRayModule extends ClientModule {
    public static volatile boolean rendering;
    public XRayModule() { super("XRay", "Render ores and selected storage blocks through terrain.", "World"); }
    public static boolean visible(BlockState state) {
        // Keep coal and iron explicit even though Forge's ORES tag normally contains them.
        // This makes these core XRay targets independent of tag composition changes.
        return state.is(Blocks.COAL_ORE) || state.is(Blocks.DEEPSLATE_COAL_ORE)
            || state.is(Blocks.IRON_ORE) || state.is(Blocks.DEEPSLATE_IRON_ORE)
            || state.is(Tags.Blocks.ORES) || state.is(Blocks.ANCIENT_DEBRIS) || state.is(Blocks.CHEST)
            || state.is(Blocks.TRAPPED_CHEST) || state.is(Blocks.ENDER_CHEST) || state.is(Blocks.SPAWNER);
    }
    private void update(Minecraft mc, boolean value) {
        if (rendering == value) return;
        rendering = value;
        if (mc.level != null) mc.levelRenderer.allChanged();
    }
    @Override public void activate(Minecraft mc) { update(mc, true); }
    @Override public void tick(Minecraft mc) { update(mc, true); }
    @Override public void reset(Minecraft mc) { update(mc, false); }
}
