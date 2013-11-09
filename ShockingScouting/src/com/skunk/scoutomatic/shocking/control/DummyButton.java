package com.skunk.scoutomatic.shocking.control;

import com.skunk.scoutomatic.shocking.FieldActivity;

public class DummyButton extends FieldButton {
	protected DummyButton(String s) {
		super(s);
	}

	@Override
	public boolean isVisible(FieldActivity frontend) {
		return true;
	}
}
