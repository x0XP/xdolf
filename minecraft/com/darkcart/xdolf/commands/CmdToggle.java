package com.darkcart.xdolf.commands;

import com.darkcart.xdolf.Module;
import com.darkcart.xdolf.Wrapper;
import com.darkcart.xdolf.mods.Hacks;

public class CmdToggle extends Command {
	public CmdToggle() {
		super("toggle");
	}

	@Override
	public void runCommand(String s, String[] args) {
		Module mod = Hacks.getMod(args[0]);

		if (mod != null) {
			mod.toggle();
			Wrapper.addChatMessage("Toggled " + mod.getName() + (mod.isEnabled() ? " on." : " off."));
		}
		else {
			Wrapper.addChatMessage("Invalid mod.");
		}
	}

	@Override
	public String getDescription() {
		return "Toggles the specified hack";
	}

	@Override
	public String getSyntax() {
		return "toggle <name of hack>";
	}
}
