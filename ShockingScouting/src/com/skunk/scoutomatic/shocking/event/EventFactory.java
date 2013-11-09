package com.skunk.scoutomatic.shocking.event;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class EventFactory implements OnTouchListener {
	private long heldStart = -1;
	private float heldBeginX, heldBeginY;
	private final SpecialEventListener onUpdate;

	public EventFactory(SpecialEventListener onUpdate) {
		this.onUpdate = onUpdate;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (heldStart < 0) {
				heldStart = event.getEventTime();
				heldBeginX = event.getX();
				heldBeginY = event.getY();
			}
			break;
		case MotionEvent.ACTION_UP:
			if (heldStart > 0) {
				if (onUpdate != null) {
					onUpdate.onSpecialTouch(new SpecialDragEvent(heldStart,
							event.getEventTime() - heldStart, heldBeginX,
							heldBeginY, event.getX(), event.getY()));
				}
				heldStart = -1;
			}
			break;
		}
		return false;
	}
}
