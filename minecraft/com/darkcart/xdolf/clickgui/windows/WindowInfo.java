package com.darkcart.xdolf.clickgui.windows;

import java.text.DecimalFormat;

import com.darkcart.xdolf.Wrapper;
import com.darkcart.xdolf.clickgui.elements.XdolfTextWindow;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;

public class WindowInfo extends XdolfTextWindow {

	static final DecimalFormat ANGLE = new DecimalFormat("0.0");
	static final DecimalFormat COORD = new DecimalFormat("#,##0");

	public WindowInfo() {
		super("Info", 2, 17);
	}

	@Override
	protected String[] getText() {
		EnumFacing direction = Wrapper.getPlayer().getHorizontalFacing();
		String directionCoord;
		switch (direction) {
			case NORTH: directionCoord = "-Z"; break;
            case SOUTH: directionCoord = "+Z"; break;
            case WEST: directionCoord = "-X"; break;
            case EAST: directionCoord = "+X"; break;
			default: directionCoord = "Invalid"; // UP & DOWN
		}

		String[] lines = {
				Wrapper.getMinecraft().getDebugFPS() + " FPS",
				"X: " + COORD.format(Math.floor(Wrapper.getPlayer().posX)),
				"Y: " + COORD.format(Math.floor(Wrapper.getPlayer().posY)),
				"Z: " + COORD.format(Math.floor(Wrapper.getPlayer().posZ)),
				"Facing: " + direction.toString().toUpperCase() + " [" + directionCoord + "]",
				"Yaw: " + ANGLE.format(MathHelper.wrapDegrees(Wrapper.getPlayer().rotationYaw)) +
					" Pitch: " + ANGLE.format(MathHelper.wrapDegrees(Wrapper.getPlayer().rotationPitch)),
		};

		// modify line 1 and 3 if in hell to append overworld coords.
		// could also have done + (hell ? "overworld" : "") in the string initializer, but this felt easier to read
		if (Wrapper.getPlayer().dimension == -1) {
			lines[1] += " [" + COORD.format(Math.floor(Wrapper.getPlayer().posX * 8.0)) + "]";
			lines[3] += " [" + COORD.format(Math.floor(Wrapper.getPlayer().posZ * 8.0)) + "]";
		}

		return lines;
	}
}
