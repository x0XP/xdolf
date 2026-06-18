package com.darkcart.xdolf.mods.player;

import org.lwjgl.input.Keyboard;

import com.darkcart.xdolf.Module;
import com.darkcart.xdolf.util.Category;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;

public class AntiHunger extends Module {

	public AntiHunger() {
		super("AntiHunger", "Reduce hunger caused by activity.", Keyboard.KEYBOARD_SIZE, 0xFFFFFF, Category.PLAYER);
	}

	@Override
	public Packet<?> onPacketSend(Packet<?> packet) {
		if (packet instanceof CPacketPlayer) {
            ((CPacketPlayer) packet).onGround = false;
        }

        return packet;
	}

}
