package com.darkcart.xdolf.mods.player;

import com.darkcart.xdolf.Module;
import com.darkcart.xdolf.Wrapper;
import com.darkcart.xdolf.util.Category;
import com.darkcart.xdolf.util.Option;
import com.darkcart.xdolf.util.Value;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Keyboard;

public class AutoFish extends Module {

	private static long lastAutocastTime = System.currentTimeMillis(); // Reset
																		// on
																		// enable,
																		// on
																		// autocast
																		// toggle
																		// and
																		// every
																		// 5
																		// seconds
	private static long lastRecastTime = System.currentTimeMillis();
	private boolean catching = false;
	private int lastY = 0;

	public static Value autocastDelay = new Value("Auto Cast Delay");
	public static Value recastDelay = new Value("Recast Delay");
	public static Option autocast = new Option("Auto Cast", false) {
		@Override
		public void toggle() {
			super.toggle();
			lastAutocastTime = System.currentTimeMillis();
		}
	};
	public static Option recast = new Option("Recaster", true) {
		@Override
		public void toggle() {
			super.toggle();
			lastRecastTime = System.currentTimeMillis();
		}
	};

	public AutoFish() {
		super("AutoFish", "Gotta catch 'em all!", Keyboard.KEY_NONE, 0xFFFFFF, Category.PLAYER);
	}

	@Override
	public void onEnable() {
		lastAutocastTime = lastRecastTime = System.currentTimeMillis();
	}

	@Override
	public void onUpdate(EntityPlayerSP player) {
		// if catching, the thread is running so wait for it to end
		if (catching)
			return;
		if (player != null && player.fishEntity != null) {
			long currentTime = System.currentTimeMillis(), recastDiff = currentTime - lastRecastTime,
					autocastDiff = currentTime - lastAutocastTime;
			int currentY = (int) Math.floor(player.fishEntity.posY);

			// if a fishing hook exists, keep track of it's y and reel it in if
			// it's y changes
			if (player.fishEntity != null) {
				try {
					if (lastY == 0) {
						lastY = currentY;
					}
					if (lastY != currentY || (recast.isEnabled() && recastDiff > recastDelay.getValue() * 1000)) {
						lastY = currentY;
						catching = true;
						new Thread(() -> {
							useRod();
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							useRod();
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							lastY = 0;
							catching = false;
						}).start();
					}
				} catch (Exception ex) {
					// Probably if you had the mod on when closing mc and
					// starting
					// it, it will crash when joining a world
					lastY = 0;
					catching = false;
				}
			}
			// if autocast has been enabled for 5 seconds and the rod isn't cast
			// in that time/now
			else if (autocast.isEnabled() && autocastDiff > autocastDelay.getValue() * 1000) {
				useRod();
			}
		}
	}

	private void useRod() {
		Item main = Wrapper.getPlayer().getHeldItemMainhand().getItem(),
				off = Wrapper.getPlayer().getHeldItemOffhand().getItem();

		// Use processRightClick directly instead of mc.rightClickMouse();
		if (main instanceof ItemFishingRod) {
			Wrapper.getMinecraft().playerController.processRightClick(Wrapper.getPlayer(), Wrapper.getWorld(),
					EnumHand.MAIN_HAND);
		} else if (off instanceof ItemFishingRod) {
			Wrapper.getMinecraft().playerController.processRightClick(Wrapper.getPlayer(), Wrapper.getWorld(),
					EnumHand.OFF_HAND);
		} else {
			return;
		}

		lastAutocastTime = lastRecastTime = System.currentTimeMillis();
	}
}
