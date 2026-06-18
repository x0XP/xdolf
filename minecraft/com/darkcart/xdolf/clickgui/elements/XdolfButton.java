package com.darkcart.xdolf.clickgui.elements;

import org.lwjgl.input.Mouse;

import com.darkcart.xdolf.Module;
import com.darkcart.xdolf.Wrapper;
import com.darkcart.xdolf.clickgui.XdolfGuiClick;
import com.darkcart.xdolf.clickgui.windows.WindowOptions;
import com.darkcart.xdolf.fonts.Fonts;
import com.darkcart.xdolf.util.RenderUtils;


public class XdolfButton {
	
	private XdolfWindow window;
	private Module mod;
	private int x, y;
	public boolean overButton;
	
	public XdolfButton(XdolfWindow window, Module mod, int x, int y) {
		this.window = window;
		this.mod = mod;
		this.x = x;
		this.y = y;
	}
	
	public void draw() {
		RenderUtils.drawBorderedRect(x + window.getDragX() + 95, y + window.getDragY(), x + 96 + window.getDragX(), y + 12  + window.getDragY(), 0.5F, 0x00000000, mod.isEnabled() ? overButton ? 0xFF44AAFF : 0xFFFF0000 : overButton ? 0xFF888888 : 0x0033363d);
		
		if(mod.getOptions().size() > 0) {
			Fonts.roboto18.drawStringWithShadow("+", (int)(x + 90 + window.getDragX()), (int)y + window.getDragY(), mod.isEnabled() ? overButton ? 0xFF44AAFF : 0xFFFFFF : overButton ? 0xFF888888 : 0xFFFFFF);
		}
		Wrapper.drawCenteredTTFString(mod.getName(), (int)(x + 48 + window.getDragX()), (int)y + window.getDragY(), mod.isEnabled() ? overButton ? 0xFF44AAFF : 0xFFFFFF : overButton ? 0xFF888888 : 0x99FFFFFF);
	}
	
	public void mouseClicked(int x, int y, int button) {
		if(x >= getX() + window.getDragX() && y >= getY() + window.getDragY() && x <= getX() + 96 + window.getDragX() && y <= getY() + 11 + window.getDragY() && window.isOpen()) {
			XdolfGuiClick.sendPanelToFront(window);
			if(button == 0) {
				mod.toggle();
			} else {
				if(mod.getOptions().size() <= 0)
					return;
				
				for(XdolfWindow w : XdolfGuiClick.windowList) {
					if(w instanceof WindowOptions) {
						if(w.getTitle().equalsIgnoreCase(mod.getName())) {
							XdolfGuiClick.windowList.remove(w);
							return;
						}else{
							XdolfGuiClick.windowList.remove(w);
						}
					}
				}
				new WindowOptions(mod);
			}
		}
	}
	
	public Module getModule() {
		return mod;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
}
