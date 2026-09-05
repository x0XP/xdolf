package com.darkcart.xdolf;

import net.minecraft.client.Minecraft;

/** Module state is owned by the client thread; no background game mutations. */
public abstract class ClientModule {
    public final String name;
    public final String description;
    public final String category;
    private boolean enabled;
    public int key = -1;

    protected ClientModule(String name, String description, String category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public final boolean enabled() { return enabled; }

    public final void setEnabled(boolean value) {
        if (enabled == value) return;
        enabled = value;
        if (!value) reset(Minecraft.getInstance());
    }

    public abstract void tick(Minecraft mc);

    /** Also called on world changes and when opening a screen. Must be idempotent. */
    public void reset(Minecraft mc) {}
}
