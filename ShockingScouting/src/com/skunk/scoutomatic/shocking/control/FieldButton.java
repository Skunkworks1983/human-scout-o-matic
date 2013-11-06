package com.skunk.scoutomatic.shocking.control;

public abstract class FieldButton {
	protected final String name;

	protected FieldButton(String s) {
		this.name = s;
	}

	public String getName() {
		return name;
	}

	public abstract boolean isVisible();
}
