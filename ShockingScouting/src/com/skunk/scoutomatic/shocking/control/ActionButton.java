package com.skunk.scoutomatic.shocking.control;

import com.skunk.scoutomatic.shocking.FieldActivity;
import com.skunk.scoutomatic.util.DatabaseInstance;

public abstract class ActionButton extends FieldButton {
	protected ActionButton(String s) {
		super(s);
	}

	public abstract void run(FieldActivity instance, DatabaseInstance db);

	public boolean isVisible(FieldActivity act) {
		return true;
	}
}
