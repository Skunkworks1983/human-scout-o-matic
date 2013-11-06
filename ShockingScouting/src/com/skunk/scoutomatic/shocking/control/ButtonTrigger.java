package com.skunk.scoutomatic.shocking.control;

import com.skunk.scoutomatic.shocking.FieldActivity;

public interface ButtonTrigger {
	public boolean isActive(FieldActivity act);

	public static final ButtonTrigger HOLD_THREE_SECOND = new ButtonTrigger() {
		@Override
		public boolean isActive(FieldActivity act) {
			// TODO Auto-generated method stub
			return false;
		}
	};

	public static final ButtonTrigger SINGLE_TAP = new ButtonTrigger() {
		@Override
		public boolean isActive(FieldActivity act) {
			// TODO Auto-generated method stub
			return false;
		}
	};

	public static final ButtonTrigger DOUBLE_TAP = new ButtonTrigger() {
		@Override
		public boolean isActive(FieldActivity act) {
			// TODO Auto-generated method stub
			return false;
		}
	};
}
