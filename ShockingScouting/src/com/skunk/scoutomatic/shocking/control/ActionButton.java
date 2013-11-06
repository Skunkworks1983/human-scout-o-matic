package com.skunk.scoutomatic.shocking.control;

public abstract class ActionButton extends FieldButton {
	protected ActionButton(String s) {
		super(s);
	}

	public abstract void run(float fX, float fY, Object arg);
}
