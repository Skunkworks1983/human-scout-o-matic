package com.skunk.scoutomatic.shocking.event;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class CompositeTouchListener implements OnTouchListener {
	private final OnTouchListener[] listeners;

	public CompositeTouchListener(OnTouchListener... listeners) {
		this.listeners = listeners;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		for (int i = 0; i < listeners.length; i++) {
			if (listeners[i].onTouch(v, event)) {
				return true;
			}
		}
		return true;
	}

}
