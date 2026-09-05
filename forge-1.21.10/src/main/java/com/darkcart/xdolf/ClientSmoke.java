package com.darkcart.xdolf;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;

/** Explicit opt-in development smoke test; never runs in a normal launch. */
final class ClientSmoke {
    private static final boolean ACTIVE = Boolean.getBoolean("xdolf.smokeTest");
    private static boolean started;
    private static int frames;
    private static String lastScreen;

    static void tick(Minecraft mc) {
        if (!ACTIVE || started) return;
        String screen = mc.screen == null ? "none" : mc.screen.getClass().getSimpleName();
        if (!screen.equals(lastScreen)) {
            lastScreen = screen; LogUtils.getLogger().info("XDOLF_SMOKE_SCREEN: {}", screen);
        }
        if (mc.getOverlay() != null || (!(mc.screen instanceof TitleScreen) && !screen.equals("AccessibilityOnboardingScreen"))) return;
        started = true;
        try {
            for (String type : new String[] {"net.minecraft.client.renderer.GameRenderer", "net.minecraft.world.entity.LivingEntity",
                "net.minecraft.client.Minecraft", "net.minecraft.client.player.LocalPlayer", "net.minecraft.world.entity.player.Player",
                "net.minecraft.world.level.block.Block", "net.minecraft.client.multiplayer.MultiPlayerGameMode",
                "net.minecraft.client.multiplayer.ClientPacketListener", "net.minecraft.network.Connection",
                "net.minecraft.world.level.block.LiquidBlock", "net.minecraft.client.Camera", "net.minecraft.client.player.ClientInput",
                "net.minecraft.client.renderer.entity.LivingEntityRenderer", "net.minecraft.client.renderer.block.BlockRenderDispatcher",
                "net.minecraft.client.renderer.chunk.SectionCompiler"})
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
