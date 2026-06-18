package com.darkcart.xdolf.clickgui.windows;

import com.darkcart.xdolf.clickgui.elements.XdolfWindow;
import com.darkcart.xdolf.Module;

public class WindowOptions extends XdolfWindow
{
	public WindowOptions(Module m) {
		super(m.getName(), 0, 0);
		this.loadButtonsFromOptions(m);
		this.setOpen(true);
	}
	
	public static boolean isVisible = false;
}