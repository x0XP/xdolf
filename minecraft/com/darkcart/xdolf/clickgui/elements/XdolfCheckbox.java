package com.darkcart.xdolf.clickgui.elements;

import org.lwjgl.input.Mouse;

import com.darkcart.xdolf.Module;
import com.darkcart.xdolf.Wrapper;
import com.darkcart.xdolf.clickgui.XdolfGuiClick;
import com.darkcart.xdolf.util.Option;
import com.darkcart.xdolf.util.RenderUtils;


public class XdolfCheckbox {
	
	private XdolfWindow window;
	private Option option;
	private int x, y;
	public boolean overButton;
	
	public XdolfCheckbox(XdolfWindow window, Option option, int x, int y) {
		this.window = window;
		this.option = option;
		this.x = x;
		this.y = y;
	}
	
	public void draw() {
		RenderUtils.drawBorderedRect(x + window.getDragX() + 95, y + window.getDragY(), x + 96 + window.getDragX(), y + 12  + window.getDragY(), 0.5F, 0x00000000, option.isEnabled() ? overButton ? 0xFF44AAFF : 0xFFFF0000 : overButton ? 0xFF888888 : 0x0033363d);
		
		Wrapper.drawCenteredTTFString(option.getName(), (int)(x + 48 + window.getDragX()), (int)y + window.getDragY(), option.isEnabled() ? overButton ? 0xFF44AAFF : 0xFFFFFF : overButton ? 0xFF888888 : 0x99FFFFFF);
	}
	
	public void mouseClicked(int x, int y, int button) {
		if(x >= getX() + window.getDragX() && y >= getY() + window.getDragY() && x <= getX() + 96 + window.getDragX() && y <= getY() + 11 + window.getDragY() && window.isOpen()) {
			XdolfGuiClick.sendPanelToFront(window);
			option.toggle();
		}
	}
	
	public Option getOption() {
		return option;
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
