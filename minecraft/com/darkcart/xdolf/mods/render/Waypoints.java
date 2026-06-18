package com.darkcart.xdolf.mods.render;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.darkcart.xdolf.Module;
import com.darkcart.xdolf.Wrapper;
import com.darkcart.xdolf.util.Category;
import com.darkcart.xdolf.util.RenderUtils;
import com.darkcart.xdolf.util.Waypoint;

public class Waypoints extends Module {
	public Waypoints() {
		super("Waypoints", "Toggles waypoint rendering.", Keyboard.KEY_EQUALS, 0xFFFFFF, Category.RENDER);
	}
	
	@Override
	public void onRender() {
		for(Waypoint w : Waypoint.wayPoints) {
			if(w.dimension == Wrapper.getPlayer().dimension) {
				w.update();
				RenderUtils.drawESP(w.dX, w.dY, w.dZ, w.red, w.green, w.blue);
				RenderUtils.drawWayPointTracer(w);
			}
		}
	}
}
