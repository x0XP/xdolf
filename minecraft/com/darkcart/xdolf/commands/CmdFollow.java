package com.darkcart.xdolf.commands;

import com.darkcart.xdolf.Wrapper;
import com.darkcart.xdolf.mods.Hacks;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;

public class CmdFollow extends Command {

	public CmdFollow() {
		super("follow");
	}

	@Override
	public void runCommand(String s, String[] args) {
		for (EntityPlayer player: Wrapper.getWorld().playerEntities) {
			if (player.getName().equals(args[0])) {
				faceEntity(player);
				Hacks.getModByName("AutoWalk").toggle();
			}
		}
	}

	@Override
	public String getDescription() {
		return "Follows a player of your choice.";
	}

	@Override
	public String getSyntax() {
		return "follow <player>";
	}

	public void faceEntity(Entity entity) {
		double x = entity.posX - Wrapper.getPlayer().posX;
		double z = entity.posZ - Wrapper.getPlayer().posZ;
		double y = entity.posY + (entity.getEyeHeight() / 1.4D) - Wrapper.getPlayer().posY
				+ (Wrapper.getPlayer().getEyeHeight() / 1.4D);
		double helper = MathHelper.sqrt(x * x + z * z);

		float newYaw = (float) ((Math.toDegrees(-Math.atan(x / z))));
		float newPitch = (float) -Math.toDegrees(Math.atan(y / helper));

		if (z < 0 && x < 0) {
			newYaw = (float) (90D + Math.toDegrees(Math.atan(z / x)));
		} else if (z < 0 && x > 0) {
			newYaw = (float) (-90D + Math.toDegrees(Math.atan(z / x)));
		}

		Wrapper.getPlayer().rotationYaw = newYaw;
		Wrapper.getPlayer().rotationPitch = newPitch;
		Wrapper.getPlayer().rotationYawHead = newPitch;
	}
}
