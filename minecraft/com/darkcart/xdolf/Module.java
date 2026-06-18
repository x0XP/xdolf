package com.darkcart.xdolf;

import java.util.ArrayList;
import java.util.List;

import com.darkcart.xdolf.fonts.Fonts;
import com.darkcart.xdolf.mods.Hacks;
import com.darkcart.xdolf.util.Category;
import com.darkcart.xdolf.util.Option;

import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.Packet;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class Module {

	private String name;
	private String description;
	public Object[] original;
	private int keyBind;
	private int arrayColor = 0xffffff;

	private boolean isEnabled;
	private boolean isVisible;

	private Category category;
	
	protected ArrayList<Option> options = new ArrayList<>();

	public Module(String name, String description, Category category) {
		this.name = name;
		this.setDescription(description);
		this.isVisible = false;
		this.category = category;
		this.original = new Object[] { name };
		this.keyBind = 0;
		System.out.println("[Xdolf] " + name + " instantiated.");
	}

	public Module(String name, String description, int keyBind, Category category) {
		this.name = name;
		this.setDescription(description);
		this.keyBind = keyBind;
		this.isVisible = false;
		this.category = category;
		this.original = new Object[] { name };
		System.out.println("[Xdolf] " + name + " instantiated.");
	}

	public Module(String name, String description, int keyBind, int arrayColor, Category category) {
		this.name = name;
		this.setDescription(description);
		this.keyBind = keyBind;
		this.arrayColor = arrayColor;
		this.isVisible = true;
		this.category = category;
		this.original = new Object[] { name, arrayColor };
		System.out.println("[Xdolf] " + name + " instantiated.");
	}

	public void onEnable() {
	}

	public void onDisable() {
	}

	public void onToggled() {
	}

	public void beforeUpdate(EntityPlayerSP player) {
	}

	public void onUpdate(EntityPlayerSP player) {
	}

	public void afterUpdate(EntityPlayerSP player) {
	}

	public void runTick() {
	}

	public void onRender() {
	}
	
	public void initOptions() {
	}

	public GuiScreen onDisplayGuiScreen(GuiScreen guiScreen) {
		return guiScreen;
	}

	/**
	 * Fired just before a packet is sent
	 *
	 * @param packet the Packet to be sent
	 * @return the packet to send, or null to cancel
	 */
	public Packet<?> onPacketSend(Packet<?> packet) {
		return packet;
	}

	/**
	 * Fired when adding a collision box for a Block
	 *
	 * @param block the block to add collision for
	 * @param pos the position of the block
	 * @param collisionBox the collision box to be added
	 * @return the AxisAlignedBB to add, will be passed on to any other listeners
	 */
	public AxisAlignedBB onAddCollisionBox(Block block, BlockPos pos, AxisAlignedBB collisionBox) {
		return collisionBox;
	}

	public void onKeyPressed(int key) {
		if (key == keyBind) {
			toggle();
		}
	}

	public String getName() {
		return name;
	}

	public int getKey() {
		return keyBind;
	}

	public int getColor() {
		return arrayColor;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public boolean getVisible() {
		return isVisible;
	}

	public Category getCategory() {
		return category;
	}

	public final void setState(boolean state) {
		if (state == isEnabled) return;

		List<Module> enabledHacks = Hacks.getEnabledHacks();
		List<Module> displayedHacks = Hacks.getDisplayedHacks();

		if (isEnabled = state) {
			if (!enabledHacks.contains(this)) {
				enabledHacks.add(this);
			}
			if (isVisible && !displayedHacks.contains(this)) {
				displayedHacks.add(this);

				// Sort by string width of mod name
				displayedHacks.sort((m, m1) -> Fonts.roboto18.getStringWidth(m1.getName()) - Fonts.roboto18.getStringWidth(m.getName()));
			}
			onEnable();
		} else {
			// Slightly more efficient than `while(contains(this)) remove(this);` only finds the index once per loop.
			for (int index = enabledHacks.indexOf(this); index > -1; index = enabledHacks.indexOf(this)) {
				enabledHacks.remove(index);
			}
			for (int index = displayedHacks.indexOf(this); index > -1; index = displayedHacks.indexOf(this)) {
				displayedHacks.remove(index);
			}
			onDisable();
		}

		Wrapper.getFileManager().saveHacks();
	}

	public void setName(String s) {
		name = s;
	}

	public void setKey(int i) {
		keyBind = i;
	}

	public final void toggle() {
		setState(!isEnabled);
		onToggled();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public final ArrayList<Option> getOptions()
	{
		return options;
	}

}
