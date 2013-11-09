package com.skunk.scoutomatic.shocking.control;

import com.skunk.scoutomatic.shocking.FieldActivity;

public abstract class ActionButton extends FieldButton {
	protected ActionButton(String s) {
		super(s);
	}

	public abstract void run(FieldActivity instance);

	public boolean isVisible(FieldActivity act) {
		return true;
	}
}
