package com.darkcart.xdolf.commands;

import com.darkcart.xdolf.Wrapper;

public class CmdHide extends Command {

	public static boolean displayLogo = true;
	public static boolean displayEnabledMods = true;
	public static boolean displayPotions = true;

	public CmdHide() {
		super("hide");
	}

	@Override
	public void runCommand(String s, String[] args) {
		String elm = args[0];
		if (elm.equals("logo")) {
			displayLogo = !displayLogo;
		}
		if (elm.equals("mods")) {
			displayEnabledMods = !displayEnabledMods;
		}
		if (elm.equals("potions")) {
			displayPotions = !displayPotions;
		}
	}

	@Override
	public String getDescription() {
		return "Used to hide GUI elements.";
	}

	@Override
	public String getSyntax() {
		return "hide <logo/mods/potions>";
	}

}
