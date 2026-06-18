package com.darkcart.xdolf.mods.render;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.darkcart.xdolf.Module;
import com.darkcart.xdolf.Wrapper;
import com.darkcart.xdolf.util.Category;
import com.darkcart.xdolf.util.Option;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Tracers extends Module {

	public Tracers() {
		super("Tracers", "Draws a line to entities within render distance.", Keyboard.KEYBOARD_SIZE, 0xFFFFFF, Category.RENDER);
	}
	
	public final Option players = new Option("Players", true);
	public final Option chests = new Option("Chests", false);
	
	@Override
	public void initOptions() {
		options.add(players);
		options.add(chests);
	}

	@Override
	public void onRender() {
		try {
			GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDepthMask(false);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glLineWidth(1.5F);
			if(players.isEnabled())
			{
				for (Entity entities : Wrapper.getWorld().loadedEntityList) {
					if (entities != Wrapper.getPlayer() && entities != null) {
						if (entities instanceof EntityPlayer) {
							drawTracerLine(entities);
						}
					}
				}
			}
			if(chests.isEnabled())
			{
				for (TileEntity tile : Wrapper.getWorld().loadedTileEntityList) {
					if (tile instanceof TileEntityChest) {
						drawTracerLine(tile.getPos());
					}
				}
			}
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDisable(GL11.GL_LINE_SMOOTH);
			GL11.glPopMatrix();
		} catch (Exception e) {}
	}
	
	public static void drawTracerLine(Entity e) {
		float distance = Wrapper.getMinecraft().renderViewEntity.getDistanceToEntity(e);
		double posX = ((e.lastTickPosX + (e.posX - e.lastTickPosX) - RenderManager.renderPosX));
		double posY = ((e.lastTickPosY + (e.posY - e.lastTickPosY) - RenderManager.renderPosY));
		double posZ = ((e.lastTickPosZ + (e.posZ - e.lastTickPosZ) - RenderManager.renderPosZ));

		if (Wrapper.getFriends().isFriend(e.getName())) {
			GL11.glColor3f(0.0F, 1.0F, 0.0F);
		} else {
			if (distance <= 6F) {
				GL11.glColor3f(1.0F, 0.0F, 0.0F);
			} else if (distance <= 96F) {
				GL11.glColor3f(1.0F, (distance / 100F), 0.0F);
			} else if (distance > 96F) {
				GL11.glColor3f(0.1F, 0.6F, 255.0F);
			}
		}
		Vec3d eyes = new Vec3d(0, 0, 1).rotatePitch(-(float) Math.toRadians(Wrapper.getPlayer().rotationPitch))
				.rotateYaw(-(float) Math.toRadians(Wrapper.getPlayer().rotationYaw));

		GL11.glBegin(GL11.GL_LINE_LOOP);

		GL11.glVertex3d(eyes.xCoord, Wrapper.getPlayer().getEyeHeight() + eyes.yCoord, eyes.zCoord);
		GL11.glVertex3d(posX, posY, posZ);

		GL11.glEnd();
	}

	public static void drawTracerLine(BlockPos e) {
		float distance = (float) Wrapper.getMinecraft().renderViewEntity.getDistance(e.getX(), e.getY(), e.getZ());

		double posX = (e.getX() - RenderManager.renderPosX);
		double posY = (e.getY() - RenderManager.renderPosY);
		double posZ = (e.getZ() - RenderManager.renderPosZ);

		GL11.glColor3f(0.1F, 255.6F, 0.0F);

		Vec3d eyes = new Vec3d(0, 0, 1).rotatePitch(-(float) Math.toRadians(Wrapper.getPlayer().rotationPitch))
				.rotateYaw(-(float) Math.toRadians(Wrapper.getPlayer().rotationYaw));

		GL11.glBegin(GL11.GL_LINE_LOOP);

		GL11.glVertex3d(eyes.xCoord, Wrapper.getPlayer().getEyeHeight() + eyes.yCoord, eyes.zCoord);
		GL11.glVertex3d(posX, posY, posZ);

		GL11.glEnd();
	}

	public static void drawTracerLine(BlockPos e, float red, float green, float blue) {
		float distance = (float) Wrapper.getMinecraft().renderViewEntity.getDistance(e.getX(), e.getY(), e.getZ());

		double posX = (e.getX() - RenderManager.renderPosX);
		double posY = (e.getY() - RenderManager.renderPosY);
		double posZ = (e.getZ() - RenderManager.renderPosZ);

		GL11.glColor3f(red, green, blue);

		Vec3d eyes = new Vec3d(0, 0, 1).rotatePitch(-(float) Math.toRadians(Wrapper.getPlayer().rotationPitch))
				.rotateYaw(-(float) Math.toRadians(Wrapper.getPlayer().rotationYaw));

		GL11.glBegin(GL11.GL_LINE_LOOP);

		GL11.glVertex3d(eyes.xCoord, Wrapper.getPlayer().getEyeHeight() + eyes.yCoord, eyes.zCoord);
		GL11.glVertex3d(posX, posY, posZ);

		GL11.glEnd();
	}
}
