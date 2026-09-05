package com.darkcart.xdolf;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

final class ClientScreen extends Screen {
    ClientScreen() { super(Component.literal("Xdolf")); }

    @Override
    protected void init() {
        int buttonWidth = Math.min(300, width - 24);
        int x = (width - buttonWidth) / 2;
        int y = 52;
        for (ClientModule module : ClientRuntime.MODULES) {
            addRenderableWidget(Button.builder(label(module), button -> {
                ClientRuntime.toggle(module);
                button.setMessage(label(module));
            }).bounds(x, y, buttonWidth, 20)
                .tooltip(Tooltip.create(Component.literal(module.description))).build());
            y += 24;
        }
        addRenderableWidget(Button.builder(Component.literal("Done"), button -> onClose())
            .bounds(x, y + 6, buttonWidth, 20).build());
    }

    private Component label(ClientModule module) {
        return Component.literal(module.category + " / " + module.name + (module.enabled() ? "  [ON]" : "  [OFF]"));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.fill(0, 0, width, height, 0xE6101620);
        graphics.drawCenteredString(font, "XDOLF", width / 2, 15, 0xFF70D7FF);
        graphics.drawCenteredString(font, "Forge 1.21.10 / Development port", width / 2, 31, 0xFFB6C3D1);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() { return false; }
}
