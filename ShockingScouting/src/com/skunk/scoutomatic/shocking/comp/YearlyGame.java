package com.skunk.scoutomatic.shocking.comp;

import com.skunk.scoutomatic.shocking.FieldActivity;
import com.skunk.scoutomatic.shocking.control.ParentButton;
import com.skunk.scoutomatic.shocking.event.SpecialTouchEvent;
import com.skunk.scoutomatic.util.ObjectFilter;

public abstract class YearlyGame {
	private FieldActivity frontend;

	public YearlyGame(FieldActivity act) {
		this.frontend = act;
	}

	/**
	 * Create all the triggered buttons!
	 */
	public abstract void init();

	public void registerButtonTrigger(ObjectFilter<SpecialTouchEvent> trigger,
			ParentButton button) {
		frontend.getButtonManager().registerButtonTrigger(trigger, button);
	}
}
