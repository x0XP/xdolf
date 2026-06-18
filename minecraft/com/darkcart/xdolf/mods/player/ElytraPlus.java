package com.darkcart.xdolf.mods.player;

import com.darkcart.xdolf.Module;
import com.darkcart.xdolf.Wrapper;
import com.darkcart.xdolf.util.Category;
import com.darkcart.xdolf.util.Option;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import org.lwjgl.input.Keyboard;

import static net.minecraft.network.play.client.CPacketEntityAction.Action.START_FALL_FLYING;

public class ElytraPlus extends Module {

	public ElytraPlus() {
		super("ElytraPlus", "Vanilla flight using Elytra.", Keyboard.KEY_NONE, 0xFFFFFF, Category.PLAYER);
	}
	
	public static Option instantFly = new Option("Instant fly - easy takeoff", true);
	public static Option stopInWater = new Option("Stop in water", false);

	private long lastTime = System.currentTimeMillis();

	@Override
	public void initOptions() {
	    options.add(instantFly);
	    options.add(stopInWater);
    }
	
	@Override
	public void beforeUpdate(EntityPlayerSP player) {
		if(isEnabled()) {
		}
	}
	
	@Override
	public void onUpdate(EntityPlayerSP player) {
		if(!isEnabled()) return;

		long currentTime = System.currentTimeMillis();
		long diffTime = currentTime - lastTime;
		ItemStack elytra = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        GameSettings settings = Wrapper.getGameSettings();

		// Ensure player is wearing elytra
		if(elytra == null || elytra.getItem() != Items.ELYTRA) return;

		// If flying
		if (player.isElytraFlying()) {
		    if (stopInWater.isEnabled() && (player.isInWater() || player.isInLava())) {
		        player.connection.sendPacket(new CPacketEntityAction(player, START_FALL_FLYING));
		        return;
            }

		    if (settings.keyBindJump.isKeyDown())
		        player.motionY += 0.08;
		    else if (settings.keyBindSneak.isKeyDown())
		        player.motionY -= 0.04;

		    if (settings.keyBindForward.isKeyDown()) {
		        double yaw = Math.toRadians(player.rotationYaw);
		        player.motionX -= Math.sin(yaw) * 0.05;
		        player.motionZ += Math.cos(yaw) * 0.05;
            }
            else if (settings.keyBindBack.isKeyDown()) {
                double yaw = Math.toRadians(player.rotationYaw);
                player.motionX += Math.sin(yaw) * 0.05;
                player.motionZ -= Math.cos(yaw) * 0.05;
            }
		}
		// If not flying, but instantFly is enabled
		else if (instantFly.isEnabled() && !ItemElytra.isBroken(elytra)
                && settings.keyBindJump.isKeyDown()) {
		    // reset jump every 1000ms
			if (diffTime >= 1000) {
                lastTime = currentTime;
                player.setJumping(false);
                player.setSprinting(true);
                player.jump();
			}
			player.connection.sendPacket(new CPacketEntityAction(player, START_FALL_FLYING));
		}
	}
}