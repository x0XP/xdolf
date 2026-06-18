package com.darkcart.xdolf.commands;

import com.darkcart.xdolf.Wrapper;
import com.darkcart.xdolf.mods.Hacks;
import com.darkcart.xdolf.mods.world.XRay;
import net.minecraft.block.Block;

public class CmdXray extends Command {
	public CmdXray() {
		super("xray");
	}

	@Override
	public void runCommand(String s, String[] args) {
		if (args[0].equalsIgnoreCase("add")) {
			String name = args[1];
			if (!Hacks.getMod(XRay.class).xrayBlocks.contains(Block.getBlockFromName(name))) {
				if (Block.getBlockFromName(name) != null) {
					Hacks.getMod(XRay.class).xrayBlocks.add(Block.getBlockFromName(name));
					Wrapper.addChatMessage("Added \247e" + name + "\247f to xray list.");
					if (Hacks.getMod(XRay.class).isEnabled()) {
						Wrapper.getMinecraft().renderGlobal.loadRenderers();
					}
					Wrapper.getFileManager().saveXrayList();
				} else {
					Wrapper.addChatMessage("\247e" + name + "\247f is not a recognized block.");
				}
			} else {
				Wrapper.addChatMessage("\247e" + name + "\247f is already in the xray list.");
			}
		} else if (args[0].equalsIgnoreCase("del")) {
			String name = args[1];
			if (Hacks.getMod(XRay.class).xrayBlocks.contains(Block.getBlockFromName(name))) {
				XRay.xrayBlocks.remove(Block.getBlockFromName(name));

				Wrapper.addChatMessage("Removed \247e" + name + "\247f from xray list.");
				if (Hacks.getMod(XRay.class).isEnabled()) {
					Wrapper.getMinecraft().renderGlobal.loadRenderers();
				}
				Wrapper.getFileManager().saveXrayList();
			} else {
				Wrapper.addChatMessage("\247e" + name + "\247f is not in the xray list.");
			}
		}
	}

	@Override
	public String getDescription() {
		return "Custom xray.";
	}

	@Override
	public String getSyntax() {
		return "xray add/del <block name>";
	}
}