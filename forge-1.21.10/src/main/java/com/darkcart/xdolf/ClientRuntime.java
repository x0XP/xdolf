package com.darkcart.xdolf;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.AddGuiOverlayLayersEvent;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import org.lwjgl.glfw.GLFW;
import java.util.List;
import java.util.Locale;

final class ClientRuntime {
    static final List<ClientModule> MODULES = Modules.create();
    private static ClientLevel previousLevel;

    static void register() {
        ClientConfig.load(MODULES);
        TickEvent.ClientTickEvent.Post.BUS.addListener(ClientRuntime::tick);
        InputEvent.Key.BUS.addListener(ClientRuntime::key);
        ClientChatEvent.BUS.addListener((java.util.function.Predicate<ClientChatEvent>) ClientRuntime::chat);
        AddGuiOverlayLayersEvent.BUS.addListener(event -> event.getLayeredDraw().add(
            ResourceLocation.fromNamespaceAndPath(Xdolf.ID, "hud"), (graphics, delta) -> {
                Minecraft mc = Minecraft.getInstance();
                if (mc.player == null || mc.options.hideGui) return;
                graphics.drawString(mc.font, "Xdolf | 1.21.10 DEV", 6, 6, 0xFF70D7FF);
                int y = 19;
                for (ClientModule module : MODULES) {
                    if (!module.enabled()) continue;
                    graphics.drawString(mc.font, module.name, 6, y, 0xFFE8EDF4);
                    y += 11;
                }
            }));
    }

    private static void tick(TickEvent.ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != previousLevel) {
            for (ClientModule module : MODULES) {
                module.setEnabled(false);
                module.reset(mc);
            }
            previousLevel = mc.level;
        }
        if (mc.player == null || mc.level == null || mc.getConnection() == null) return;
        for (ClientModule module : MODULES) {
            if (!module.enabled()) continue;
            boolean respawnScreen = module.name.equals("AutoRespawn") && mc.screen instanceof DeathScreen;
            if (mc.isPaused() || (mc.screen != null && !respawnScreen)) {
                module.reset(mc);
                continue;
            }
            try {
                module.tick(mc);
            } catch (RuntimeException error) {
                module.setEnabled(false);
                LogUtils.getLogger().error("Xdolf disabled failed module {}", module.name, error);
                message(module.name + " disabled after an error; check latest.log.");
            }
        }
    }

    private static void key(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (event.getAction() != GLFW.GLFW_PRESS || mc.screen != null || mc.player == null) return;
        if (event.getKey() == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            mc.setScreen(new ClientScreen());
            return;
        }
        for (ClientModule module : MODULES) if (module.key == event.getKey()) toggle(module);
    }

    private static boolean chat(ClientChatEvent event) {
        String text = event.getMessage();
        if (!text.startsWith(".")) return false;
        // Local commands must never leak into multiplayer chat, including invalid commands.
        String[] parts = text.substring(1).trim().split("\\s+");
        switch (parts[0].toLowerCase(Locale.ROOT)) {
            case "help" -> message(".gui | .mods | .toggle <module> | .bind <module> <A-Z/F1-F12/NONE> | .alloff");
            case "gui" -> Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new ClientScreen()));
            case "mods", "modlist" -> MODULES.forEach(module -> message(module.name + (module.enabled() ? " ON" : " OFF")));
            case "alloff" -> {
                MODULES.forEach(module -> module.setEnabled(false));
                message("All modules disabled.");
            }
            case "toggle", "t" -> {
                ClientModule module = parts.length == 2 ? find(parts[1]) : null;
                if (module == null) message("Usage: .toggle <module>; use .mods for available modules.");
                else toggle(module);
            }
            case "bind" -> bind(parts);
            default -> message("Unknown local command. Use .help.");
        }
        return true;
    }

    private static void bind(String[] parts) {
        ClientModule module = parts.length == 3 ? find(parts[1]) : null;
        if (module == null) { message("Usage: .bind <module> <A-Z/F1-F12/NONE>"); return; }
        String value = parts[2].toUpperCase(Locale.ROOT);
        int key = -1;
        if (value.matches("[A-Z0-9]")) key = value.charAt(0);
        else if (value.matches("F([1-9]|1[0-2])")) key = GLFW.GLFW_KEY_F1 + Integer.parseInt(value.substring(1)) - 1;
        else if (!value.equals("NONE")) { message("Choose A-Z, 0-9, F1-F12, or NONE."); return; }
        module.key = key;
        ClientConfig.save(MODULES);
        message(module.name + " binding: " + value);
    }

    static ClientModule find(String name) {
        return MODULES.stream().filter(module -> module.name.equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    static void toggle(ClientModule module) {
        module.setEnabled(!module.enabled());
        message(module.name + (module.enabled() ? " enabled" : " disabled"));
    }

    static void message(String text) {
        var player = Minecraft.getInstance().player;
        if (player != null) player.displayClientMessage(Component.literal("[Xdolf] " + text), false);
    }
}
