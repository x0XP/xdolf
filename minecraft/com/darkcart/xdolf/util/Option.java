package com.darkcart.xdolf.util;

import com.darkcart.xdolf.Wrapper;

public class Option {
	private String name;
	private boolean isEnabled;
	
	public Option(String name, boolean isEnabled) {
		this.name = name;
		this.isEnabled = isEnabled;
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}
	
	public void setState(boolean flag) {
		isEnabled = flag;
	}
	
	public void toggle() {
		setState(!isEnabled);
		Wrapper.getFileManager().saveOptions();
	}
}
