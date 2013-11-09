package com.skunk.scoutomatic.shocking.event;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class EventFactory implements OnTouchListener {
	private long heldStart = -1;
	private float heldBeginX, heldBeginY;
	private float heldEndX, heldEndY;
	private int heldCount = 0;
	private final SpecialEventListener onUpdate;

	public EventFactory(SpecialEventListener onUpdate) {
		this.onUpdate = onUpdate;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_POINTER_DOWN:
		case MotionEvent.ACTION_DOWN:
			if (heldStart < 0) {
				heldStart = event.getEventTime();
				heldBeginX = event.getX();
				heldBeginY = event.getY();
				heldEndX = 100000;
				heldEndY = 100000;
				heldCount = event.getPointerCount();
			}
			heldCount = Math.max(heldCount, event.getPointerCount());
			break;
		case MotionEvent.ACTION_POINTER_UP:
			if (heldStart > 0) {
				float oldDist = ((heldBeginX - heldEndX) * (heldBeginX - heldEndX))
						+ ((heldBeginY - heldEndY) * (heldBeginY - heldEndY));
				float newDist = ((heldBeginX - event.getX()) * (heldBeginX - event
						.getX()))
						+ ((heldBeginY - event.getY()) * (heldBeginY - event
								.getY()));
				if (newDist < oldDist) {
					heldEndX = event.getX();
					heldEndY = event.getY();
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (heldStart > 0) {
				float oldDist = ((heldBeginX - heldEndX) * (heldBeginX - heldEndX))
						+ ((heldBeginY - heldEndY) * (heldBeginY - heldEndY));
				float newDist = ((heldBeginX - event.getX()) * (heldBeginX - event
						.getX()))
						+ ((heldBeginY - event.getY()) * (heldBeginY - event
								.getY()));
				if (newDist < oldDist) {
					heldEndX = event.getX();
					heldEndY = event.getY();
				}

				if (onUpdate != null) {
					onUpdate.onSpecialTouch(new SpecialDragEvent(heldStart,
							event.getEventTime() - heldStart, heldCount,
							heldBeginX, heldBeginY, heldEndX, heldEndY));
				}
				heldStart = -1;
			}
			break;
		}
		return false;
	}
}
