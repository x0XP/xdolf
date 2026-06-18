package com.darkcart.xdolf;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.darkcart.xdolf.clickgui.XdolfGuiClick;
import com.darkcart.xdolf.fonts.Fonts;
import com.darkcart.xdolf.mods.Hacks;
import com.darkcart.xdolf.util.FileManager;
import com.darkcart.xdolf.util.FriendManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.StringUtils;

public class Client {

	public static Minecraft mc = Minecraft.getMinecraft();
	public static ScaledResolution gameResolution = new ScaledResolution(Wrapper.getMinecraft());
	public static ArrayList<String> friends = new ArrayList<String>();

	public static final String CLIENT_NAME = "Xdolf";
	public static final String CLIENT_VERSION = "3.0.0";
	
	public static HashMap<String, String> vTable = new HashMap<String, String>();
	
	public static int protocol = 340;

	public static void onStart() {

		try {
			System.out.println("[Xdolf] Initialising Xdolf...");
			vTable.put(StringUtils.stripControlCodes(CLIENT_NAME),
					StringUtils.stripControlCodes(CLIENT_NAME) + " v" + CLIENT_VERSION);
			vTable.put("minecraft", "Minecraft 1.12.2");

			Fonts.loadFonts();

			Hacks.getHacks().sort((m, m1) -> {
                if (m.getName() == null) {
                    return (m1.getName() == null) ? 0 : +1;
                } else {
                    return (m1.getName() == null) ? -1 : m.getName().compareTo(m1.getName());
                }
            });
			
			for (Module m : Hacks.getHacks()) {
				m.initOptions();
			}

			Wrapper.friendManager = new FriendManager();
			Wrapper.fileManager = new FileManager();
			Wrapper.clickGui = new XdolfGuiClick();

			System.out.println("[Xdolf] Initialised Xdolf.");
		} catch (Exception err) {
			System.out.println("[Xdolf] Failed to initialise Xdolf, tell x0XP or Sgt Pepper." + err.toString());
			err.printStackTrace();

			String logString = "FT|CrashLog\r\n[PLAIN]\r\n---Begin plain text---\r\n";
			logString += "Console Log:\r\n";
			logString += "[Xdolf] Failed to initialise Xdolf! Expect problems! " + err.toString() + "\r\n\r\n";
			for (StackTraceElement ele : err.getStackTrace()) {
				logString += ele.getClassName() + "|" + ele.getLineNumber() + "  " + ele.toString() + "\r\n";
			}
			Wrapper.getFileManager().writeCrash(logString);
		}
	}
	
	private static String getRandomCharacters() {
		String chars = "abcdefhijklmnopqrstuvxyz0123456789";
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < 16; i++) {
			b.append(chars.toCharArray()[new Random().nextInt(chars.length())]);
		}
		return b.toString();
	}

	public static String downloadString(String uri) {
		try {
			URL url = new URL(uri);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

			String text = "";

			String line = "";
			while ((line = reader.readLine()) != null) {
				String curLine = line;
				text += curLine;
			}
			return text;
		} catch (Exception e) {
			return "Failed to retrieve string.";
		}
	}

	public static String wrap(String in, int len) {
		in = in.trim();
		if (in.length() < len)
			return in;
		if (in.substring(0, len).contains("\n"))
			return in.substring(0, in.indexOf("\n")).trim() + "\n\n" + wrap(in.substring(in.indexOf("\n") + 1), len);
		int place = Math.max(Math.max(in.lastIndexOf(" ", len), in.lastIndexOf("\t", len)), in.lastIndexOf("-", len));
		return in.substring(0, place).trim() + "\n" + wrap(in.substring(place), len);
	}

	public static void keyPressed(int keycode) {
		for (Module m: Hacks.getHacks()) {
			if (m.getKey() == keycode) {
				m.toggle();
			}
		}
	}
}
