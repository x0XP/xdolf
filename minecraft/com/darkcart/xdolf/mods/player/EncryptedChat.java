package com.darkcart.xdolf.mods.player;

import com.darkcart.xdolf.Module;
import com.darkcart.xdolf.util.Category;
import com.darkcart.xdolf.util.Option;

public class EncryptedChat extends Module {
	
	Option showEncryption = new Option("Show Encryption", false);

	public EncryptedChat(String name, String description, Category category) {
		super("EncryptedChat", "Keeps undesirables from seeing your conversations", Category.PLAYER);
	}

}
