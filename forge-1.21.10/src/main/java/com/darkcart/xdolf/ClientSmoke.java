package com.darkcart.xdolf;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;

/** Explicit opt-in development smoke test; never runs in a normal launch. */
final class ClientSmoke {
    private static final boolean ACTIVE = Boolean.getBoolean("xdolf.smokeTest");
    private static boolean started;
    private static int frames;

    static void tick(Minecraft mc) {
        if (!ACTIVE || started || !(mc.screen instanceof TitleScreen)) return;
        started = true;
        try {
            for (String type : new String[] {"net.minecraft.client.renderer.GameRenderer", "net.minecraft.world.entity.LivingEntity",
                "net.minecraft.client.Minecraft", "net.minecraft.client.player.LocalPlayer", "net.minecraft.world.entity.player.Player",
                "net.minecraft.world.level.block.Block", "net.minecraft.client.multiplayer.MultiPlayerGameMode"})
                Class.forName(type);
            mc.setScreen(new ClientScreen());
        } catch (ClassNotFoundException error) { throw new IllegalStateException("Smoke test target missing", error); }
    }

    static void frame() {
        if (ACTIVE && started && ++frames == 5) {
            LogUtils.getLogger().info("XDOLF_SMOKE_OK: mixin targets loaded and module menu rendered");
            Minecraft.getInstance().stop();
        }
    }
}
