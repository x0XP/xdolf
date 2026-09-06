package com.darkcart.xdolf;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import com.darkcart.xdolf.mixin.CreateWorldScreenAccess;

/** Explicit opt-in CI test. Normal launches never create a test world. */
final class ClientSmoke {
    private static final boolean ACTIVE = Boolean.getBoolean("xdolf.smokeTest");
    private static int phase, frames, ticks;
    static void tick(Minecraft mc) {
        if (!ACTIVE || mc.getOverlay() != null) return;
        if (phase == 0 && mc.screen != null && (mc.screen instanceof TitleScreen || mc.screen.getClass().getSimpleName().equals("AccessibilityOnboardingScreen"))) {
            phase = 1;
            mc.options.guiScale().set(2);
            org.lwjgl.glfw.GLFW.glfwSetWindowSize(org.lwjgl.glfw.GLFW.glfwGetCurrentContext(), 1280, 800);
            mc.options.renderDistance().set(3);
            mc.options.simulationDistance().set(3);
            mc.setScreen(new ClientScreen());
        } else if (phase == 2 && mc.screen instanceof CreateWorldScreen create) {
            create.getUiState().setName("Xdolf automated smoke");
            create.getUiState().setGameMode(WorldCreationUiState.SelectedGameMode.CREATIVE);
            phase = 3;
            ((CreateWorldScreenAccess) create).xdolf$create();
        } else if (phase == 3 && mc.player != null && mc.level != null && mc.screen == null) {
            if (++ticks == 30) {
                for (String name : new String[] {"Fullbright", "NoHurtCam", "Chams", "XRay", "EntityESP", "StorageESP", "Nametags", "Tracers", "Trajectories"}) ClientRuntime.find(name).setEnabled(true);
            }
            if (ticks == 180) {
                for (ClientModule module : ClientRuntime.MODULES) module.setEnabled(false);
                LogUtils.getLogger().info("XDOLF_SMOKE_WORLD_OK: singleplayer loaded and visual modules ran for 150 ticks");
                phase = 4; frames = 0; mc.setScreen(new ClientScreen());
            }
        }
    }
    static void frame() {
        if (!ACTIVE) return;
        frames++;
        if (phase == 4 && frames == 15) {
            try {
                var image = new java.awt.Robot().createScreenCapture(new java.awt.Rectangle(java.awt.Toolkit.getDefaultToolkit().getScreenSize()));
                javax.imageio.ImageIO.write(image,"png",new java.io.File("gui-smoke.png"));
            } catch (Exception error) { throw new IllegalStateException("GUI screenshot failed",error); }
            LogUtils.getLogger().info("XDOLF_SMOKE_OK: original GUI controls and singleplayer world passed");
            Minecraft.getInstance().stop();
            return;
        }
        if(frames != 5) return;
        Minecraft mc = Minecraft.getInstance();
        if (phase == 1) {
            LogUtils.getLogger().info("XDOLF_SMOKE_MENU_OK");
            phase = 2;
            mc.execute(() -> CreateWorldScreen.openFresh(mc, () -> { throw new IllegalStateException("World creation cancelled"); }));
        } else if (phase == 4) {
            ClientScreen.smokeCheckAndArrange();
        }
    }
}
