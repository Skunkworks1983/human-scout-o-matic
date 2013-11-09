package com.skunk.scoutomatic.shocking.control;

import com.skunk.scoutomatic.shocking.FieldActivity;

public abstract class FieldButton {
	protected final String name;

	protected FieldButton(String s) {
		this.name = s;
	}

	public String getName() {
		return name;
	}

	public abstract boolean isVisible(FieldActivity frontend);
}
