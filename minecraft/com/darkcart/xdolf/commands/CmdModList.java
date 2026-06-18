package com.darkcart.xdolf.commands;

import com.darkcart.xdolf.Module;
import com.darkcart.xdolf.Wrapper;
import com.darkcart.xdolf.mods.Hacks;

public class CmdModList extends Command
{
	public CmdModList()
	{
		super("modlist");
	}

	@Override
	public void runCommand(String s, String[] args)
	{
		for(Module mod: Hacks.getHacks())
		{
			if(!mod.getDescription().isEmpty()) {
				Wrapper.addChatMessage(mod.getName().replace("<", "<\247a").replace(">", "\247f>") + " - " + mod.getDescription());
			}
		}
	}

	@Override
	public String getDescription()
	{
		return "Lists all modules.";
	}

	@Override
	public String getSyntax()
	{
		return "modlist";
	}
}