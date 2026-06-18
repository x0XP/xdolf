package com.darkcart.xdolf.commands;

import com.darkcart.xdolf.Client;
import com.darkcart.xdolf.Wrapper;

import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.util.text.TextComponentString;

public class CmdSay extends Command {
	public CmdSay() {
		super("say");
	}

	@Override
	public void runCommand(String s, String[] args) {
		String s1 = s.substring(4);
		Wrapper.getPlayer().connection.sendPacket(new CPacketChatMessage(s1));
	}

	@Override
	public String getDescription() {
		return "Sends a chat message.";
	}

	@Override
	public String getSyntax() {
		return "say <message>";
	}
}