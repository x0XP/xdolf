package com.darkcart.xdolf.clickgui.elements;

import java.text.DecimalFormat;

import org.lwjgl.opengl.GL11;

import com.darkcart.xdolf.Wrapper;
import com.darkcart.xdolf.clickgui.XdolfGuiClick;
import com.darkcart.xdolf.fonts.Fonts;
import com.darkcart.xdolf.mods.Hacks;
import com.darkcart.xdolf.util.RenderUtils;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;

public abstract class XdolfTextWindow extends XdolfWindow {
	
	public XdolfTextWindow(String title, int x, int y) {
		super(title, x, y);
	}

	protected abstract String[] getText();

	@Override
	public void draw(int x, int y) {
		GL11.glPushMatrix();
		GL11.glPushAttrib(8256);
		if (dragging) {
			drag(x, y);
		}
		
		int width = 100, height = 14;
		String[] lines = null;

		// Generate lines and modify width and height
		// Only bother to generate them if open
		if (isOpen()) {
			lines = getText();
			height = (lines.length * 10) + 16;
		}
		
		// Start actually rendering, using the generated lines, width and height
		
		RenderUtils.drawBetterBorderedRect(getXAndDrag(), getYAndDrag(), getXAndDrag() + width, getYAndDrag() + height,
				0.5F, 0xFF000000, 0x80000000);

		Fonts.roboto18.drawStringWithShadow(getTitle(), getXAndDrag() + 3, getYAndDrag() + 1, 0xFFFFFFFF);
		
		// lines
		if (lines != null && lines.length > 0) {
			int offset = 3;
			for (String line : lines) {
				offset += 10;
				Fonts.roboto18.drawStringWithShadow(Fonts.roboto18.trimStringToWidth(line, width - 3), getXAndDrag() + 3, getYAndDrag() + offset, 0xFFFFFF);
			}
		}

		// Window controls
		if (Wrapper.getMinecraft().currentScreen instanceof XdolfGuiClick) {
			RenderUtils.drawBetterBorderedRect(getXAndDrag() + 79, getYAndDrag() + 2, getXAndDrag() + 88,
					getYAndDrag() + 11, 0.5F, 0xFF000000, isPinned() ? 0xFFFF0000 : 0xFF383b42);
			RenderUtils.drawBetterBorderedRect(getXAndDrag() + 89, getYAndDrag() + 2, getXAndDrag() + 98,
					getYAndDrag() + 11, 0.5F, 0xFF000000, isOpen() ? 0xFFFF0000 : 0xFF383b42);
		}

		GL11.glPopMatrix();
		GL11.glPopAttrib();
	}
}
