package com.darkcart.xdolf.commands;

import com.darkcart.xdolf.Wrapper;

import net.minecraft.entity.player.EntityPlayer;

public class CmdView extends Command {
	public CmdView() {
		super("view");
	}

	@Override
	public void runCommand(String s, String[] args) {
		if (args[0].equalsIgnoreCase("off")) {
			Wrapper.getMinecraft().renderViewEntity = Wrapper.getPlayer();
			Wrapper.addChatMessage("Now viewing normally.");
			return;
		}
		for (Object o : Wrapper.getWorld().loadedEntityList) {
			if (o instanceof EntityPlayer) {
				EntityPlayer e = (EntityPlayer) o;
				if (e.getName().equalsIgnoreCase(args[0])) {
					Wrapper.getMinecraft().renderViewEntity = e;
				}
			}
		}
	}

	@Override
	public String getDescription() {
		return "Lets you see another player's camera";
	}

	@Override
	public String getSyntax() {
		return "view <name>";
	}
}