package com.darkcart.xdolf.mods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.darkcart.xdolf.Module;
import com.darkcart.xdolf.mods.aura.*;
import com.darkcart.xdolf.mods.player.*;
import com.darkcart.xdolf.mods.render.*;
import com.darkcart.xdolf.mods.world.*;

public class Hacks
{
	
	/** All mods stored in this list **/
	private static List<Module> hackList = Arrays.asList(
			new AutoFish(),
			new Fullbright(),
			new Tracers(),
			new StorageESP(),
			new EntityESP(),
			new NoHurtCam(),
			new AntiVelocity(),
			new Flight(),
			new Spammer(),
			new Timer(),
			new XRay(),
			new KillAura(),
			new AutoRespawn(),
			new AutoArmor(),
			new AutoWalk(),
			new Chams(),
			new GUI(),
			new SafeWalk(),
			new AutoLog(),
			new NoSlowdown(),
			//new Waypoints(),
			new FastPlace(),
			//new AutoTotem(),
			new HorseJump(),
			new Sprint(),
			new Trajectories(),
			new CrystalAura(),
			new Freecam(),
			new Nametags(),
			new Criticals(),
			new NoFall(),
			new CrystalLog(),
			new AntiHunger(),
			new AutoEat(),
			new Jesus(),
			new EntitySpeed(),
			new Speedmine(),
			new EntityStep(),
			new ElytraFly(),
			new ElytraPlus()
	);

	// Instead of filtering hackList on every getEnabledHacks(),
	// instead keep track of enabled/disabled mods using enabledHackList.
	// Module.setState() adds/removes the mod from enabledHackList.
	private static List<Module> enabledHackList = new ArrayList<>();

	private static List<Module> displayedHacks = new ArrayList<>();

	public static List<Module> getHacks()
	{
		return hackList;
	}

	public static List<Module> getEnabledHacks()
	{
		return enabledHackList;
	}

	public static List<Module> getDisplayedHacks() {
		return displayedHacks;
	}
	
	public static Module getModByClassName(String name)
	{
		for(Module mod: hackList) 
		{
			if(mod.getClass().getSimpleName().toLowerCase().trim().equals(name.toLowerCase().trim()))
			{
				return mod;
			}
		}
		
		return null;
	}
	
	public static Module getModByName(String name) 
	{
		for(Module mod: hackList)
		{
			if(mod.getName().trim().equalsIgnoreCase(name.trim()) || mod.toString().trim().equalsIgnoreCase(name.trim())) 
			{
				return mod;
			}
		}
		
		return null;
	}
	
	public static <T extends Module> T getMod(Class<T> clazz)
	{
		for(Module mod: hackList)
		{
			if(mod.getClass() == clazz)
			{
				return clazz.cast(mod);
			}
		}
		
		return null;
	}
	
	public static Module getMod(String name)
	{
		Module mod = getModByName(name);
		if(mod != null) 
		{
			return mod;
		}
		mod = getModByClassName(name);
		
		return mod;
	}

	public static void onKeyPressed(int key) {
		for(Module mod : getEnabledHacks()) {
			mod.onKeyPressed(key);
		}
	}
}