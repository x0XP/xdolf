package com.darkcart.xdolf.mods.render;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import com.darkcart.xdolf.Client;
import com.darkcart.xdolf.Module;
import com.darkcart.xdolf.Wrapper;
import com.darkcart.xdolf.util.Category;
import com.darkcart.xdolf.util.Option;
import com.darkcart.xdolf.util.RenderUtils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;

public class EntityESP extends Module {

	public EntityESP() {
		super("EntityESP", "Creates an ESP box/outline around entities.", Keyboard.KEYBOARD_SIZE, 0xFFFFFF, Category.RENDER);
	}
	
	public final static Option players = new Option("Players", true);
	public final static Option monsters = new Option("Monsters", true);
	public final static Option passive = new Option("Passive", true);
	public final static Option items = new Option("Items", true);
	public final static Option outline = new Option("Outline", true);
	
	@Override
	public void initOptions() {
		options.add(players);
		options.add(monsters);
		options.add(passive);
		options.add(items);
		options.add(outline);
	}
	
	@Override
	public void onRender() {
		if(!outline.isEnabled()) {
			for (Entity e: Wrapper.getWorld().loadedEntityList) {
				String entityPackage = e.getClass().getPackage().getName();
				if(monsters.isEnabled()) {
					if (entityPackage.equals("net.minecraft.entity.monster") || e instanceof EntityWither || e instanceof EntityDragon) {
						RenderUtils.drawEntityESP(e, Color.red);
					}
				}
				if(passive.isEnabled()) {
					if (entityPackage.equals("net.minecraft.entity.passive")) {
						RenderUtils.drawEntityESP(e, Color.green);
					}
				}
				if(items.isEnabled()) {
					if (entityPackage.equals("net.minecraft.entity.item")) {
						RenderUtils.drawEntityESP(e, Color.green);
					}
				}
				if(players.isEnabled()) {
					if (e != Wrapper.getPlayer() && e != null) {
						if (e instanceof EntityPlayer) {
							if(Wrapper.getFriends().isFriend(e.getName())) {
								RenderUtils.drawEntityESP(e, Color.blue);
							}else{
								RenderUtils.drawEntityESP(e, Color.red);
							}
						}
					}
				}
			}
		}
	}
}
