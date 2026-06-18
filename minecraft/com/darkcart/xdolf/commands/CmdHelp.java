package com.darkcart.xdolf.commands;

import com.darkcart.xdolf.Wrapper;

public class CmdHelp extends Command
{
	public CmdHelp()
	{
		super("help");
	}

	@Override
	public void runCommand(String s, String[] args)
	{
		for(Command cmd: CommandManager.COMMANDS)
		{
			if(cmd != this) {
				Wrapper.addChatMessage(cmd.getSyntax().replace("<", "<\247a").replace(">", "\247f>") + " - " + cmd.getDescription());
			}
		}
	}

	@Override
	public String getDescription()
	{
		return "Lists all commands";
	}

	@Override
	public String getSyntax()
	{
		return "help";
	}
}