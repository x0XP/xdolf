package com.darkcart.xdolf.gui;

import com.darkcart.xdolf.Wrapper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;

public class GuiReconnect {
    private static ServerData lastServer;
    private static long disconnectTime;
    private static final short delay = 5 * 1000; // 5 seconds
    private static boolean enabled = true;

    // TODO call inject when making GuiDisconnected
    public static void inject(GuiDisconnected gui) {
        reset();

        GuiButton rtnButton = gui.buttonList.get(0);
        gui.addButton(new GuiButton(1, rtnButton.xPosition, rtnButton.yPosition + 24, generateButtonName()));
        gui.addButton(new GuiButton(2, rtnButton.xPosition, rtnButton.yPosition + 48, "Reconnect"));
    }

    // TODO call onUpdate on each update
    public static void onUpdate(GuiDisconnected gui) {
        gui.buttonList.get(1).displayString = generateButtonName();

        // Reconnect if enabled and delay is complete
        if (enabled && timeSinceDisconnect() > delay) {
            reconnect(gui);
        }
    }

    // TODO call on connect when connecting (GuiMultiplayer#connectToServer())
    public static void onConnect(ServerData server) {
        lastServer = server;
    }

    // TODO call from GuiDisconnected#actionButton()
    public static void onButtonAction(GuiDisconnected gui, GuiButton button) {
        switch (button.id) {
            case 1: toggleEnabled(); break;
            case 2: reconnect(gui); break;
        }
    }

    private static void toggleEnabled() {
        enabled = !enabled;
        reset();
        // TODO save enabled state to config
    }

    private static void reset() {
        disconnectTime = System.currentTimeMillis();
    }

    private static long timeSinceDisconnect() {
        return System.currentTimeMillis() - disconnectTime;
    }

    private static String generateButtonName() {
        if (enabled) return "Auto Reconnect: On (" + (timeSinceDisconnect() / 1000) + ")";
        return "Auto Reconnect: Off";
    }

    private static void reconnect(GuiDisconnected gui) {
        if (lastServer != null) {
            Wrapper.getMinecraft().displayGuiScreen(new GuiConnecting(gui.parentScreen, Wrapper.getMinecraft(), lastServer));
        }
    }
}
