package com.darkcart.xdolf.commands;

import com.darkcart.xdolf.Wrapper;

public class CmdVclip extends Command
{
	public CmdVclip()
	{
		super("vclip");
	}
	@Override
	public void runCommand(String s, String[] args)
	{
		int y = Integer.parseInt(args[0]);
		if(Wrapper.getPlayer().getRidingEntity() != null) {
			Wrapper.getPlayer().getRidingEntity().setEntityBoundingBox(Wrapper.getPlayer().getRidingEntity().getEntityBoundingBox().offset(0, y, 0));
		}else{
			Wrapper.getPlayer().setEntityBoundingBox(Wrapper.getPlayer().getEntityBoundingBox().offset(0, y, 0));
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
		return "vclip";
	}
}