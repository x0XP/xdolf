package com.darkcart.xdolf;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

final class ClientScreen extends Screen {
    private int page;
    private String category = "All";
    ClientScreen() { super(Component.literal("Xdolf")); }

    @Override
    protected void init() {
        int panel = Math.min(420, width - 24);
        int x = (width - panel) / 2;
        String[] categories = {"All", "Player", "Combat", "World", "Render"};
        int tabWidth = panel / categories.length;
        for (int i = 0; i < categories.length; i++) {
            String name = categories[i];
            addRenderableWidget(Button.builder(Component.literal(name), button -> {
                category = name; page = 0; rebuildWidgets();
            }).bounds(x + i * tabWidth, 44, tabWidth - 2, 20).build());
        }
        var modules = ClientRuntime.MODULES.stream().filter(m -> category.equals("All") || m.category.equals(category)).toList();
        int rows = Math.max(1, (height - 115) / 24);
        int pages = Math.max(1, (modules.size() + rows - 1) / rows);
        page = Math.min(page, pages - 1);
        for (int i = page * rows; i < Math.min(modules.size(), (page + 1) * rows); i++) {
            var module = modules.get(i);
            int y = 72 + (i % rows) * 24;
            addRenderableWidget(Button.builder(label(module), button -> {
                ClientRuntime.toggle(module); rebuildWidgets();
            }).bounds(x, y, panel - 54, 20).tooltip(Tooltip.create(Component.literal(module.description))).build());
            addRenderableWidget(Button.builder(Component.literal("Edit"), button -> minecraft.setScreen(new SettingsScreen(this, module)))
                .bounds(x + panel - 50, y, 50, 20).build());
        }
        addRenderableWidget(Button.builder(Component.literal("<"), button -> { page--; rebuildWidgets(); })
            .bounds(x, height - 28, 40, 20).build()).active = page > 0;
        addRenderableWidget(Button.builder(Component.literal((page + 1) + " / " + pages + "  Done"), button -> onClose())
            .bounds(x + 44, height - 28, panel - 88, 20).build());
        addRenderableWidget(Button.builder(Component.literal(">"), button -> { page++; rebuildWidgets(); })
            .bounds(x + panel - 40, height - 28, 40, 20).build()).active = page < pages - 1;
    }

    private Component label(ClientModule module) {
        return Component.literal((module.enabled() ? "[ON]  " : "[OFF] ") + module.name);
    }
    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.fill(0, 0, width, height, 0xE6101620);
        graphics.drawCenteredString(font, "XDOLF", width / 2, 13, 0xFF70D7FF);
        graphics.drawCenteredString(font, "Forge 1.21.10 / Development port", width / 2, 28, 0xFFB6C3D1);
        super.render(graphics, mouseX, mouseY, partialTick);
    }
    @Override public boolean isPauseScreen() { return false; }
}
