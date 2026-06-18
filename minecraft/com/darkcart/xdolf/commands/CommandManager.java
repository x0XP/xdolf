package com.darkcart.xdolf.commands;

import java.util.Arrays;
import java.util.List;

import com.darkcart.xdolf.Wrapper;
import com.darkcart.xdolf.util.Macro;
import net.minecraft.client.gui.GuiChat;

public class CommandManager
{

	public static final char PREFIX = '.';

	public static final List<Command> COMMANDS = Arrays.asList(
			new CmdHelp(),
			new CmdToggle(),
			new CmdTimer(),
			new CmdAllOff(),
			new CmdSay(),
			new CmdModList(),
			new CmdSpam(),
			new CmdRotation(),
			new CmdView(),
			new CmdBind(),
			new CmdFriend(),
			new CmdImpersonate(),
			new CmdXray(),
			new CmdWaypoint(),
			new CmdPraiseOre(),
			new CmdDeathCoords(),
			new CmdPlayerInfo(),
			new CmdMusic(),
			new CmdHide(),
			new CmdFollow(),
			new CmdMacro(),
			new CmdVclip()
	);
	
	public static void runCommands(String s)
	{
		if(!s.contains(Character.toString(PREFIX)) || !s.startsWith(Character.toString(PREFIX))) return;

		boolean commandResolved = false;
		String readString = s.trim().substring(Character.toString(PREFIX).length()).trim();
		boolean hasArgs = readString.trim().contains(" ");
		String commandName = hasArgs ? readString.split(" ")[0] : readString.trim();
		String[] args = hasArgs ? readString.substring(commandName.length()).trim().split(" ") : new String[0];

		for(Command command: COMMANDS)
		{
			if(command.getCommand().trim().equalsIgnoreCase(commandName.trim())) 
			{
				try {
					command.runCommand(readString, args);
				} catch (Exception e) {
					e.printStackTrace();
					Wrapper.addChatMessage("Usage: " + command.getSyntax());
				}
				commandResolved = true;
				break;
			}
		}

		if(!commandResolved)
		{
			Wrapper.addChatMessage("Invalid command. Type .help for a list of commands.");
		}
	}

	public static void onKeyPressed(int key, char character) {
		// First check the event char against our prefix char
		if (PREFIX == character) {
			// If the PREFIX char was entered, open a chat with it pre-typed
			GuiChat chat = new GuiChat(String.valueOf(PREFIX));
			Wrapper.getMinecraft().displayGuiScreen(chat);
		}

		// Now check the key against macro keybinds
		for(Macro macro : Macro.macroList) {
			if(key == macro.getKey()) {
				runCommands(macro.getCommand());
				// There may be multiple macros with one keybind, so don't break
			}
		}
	}
}