package com.skunk.scoutomatic.shocking.event;

import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.View;
import android.view.View.OnTouchListener;

public class EventFactory implements OnTouchListener {
	private long heldStart = -1;
	private float heldBeginX, heldBeginY;
	private float heldLastX, heldLastY;
	private int heldCount = 0;
	private final SpecialEventListener onUpdate;
	private float heldDistance;

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
				heldLastX = heldBeginX;
				heldLastY = heldBeginY;
				heldCount = event.getPointerCount();
				heldDistance = 0;
			}
			heldCount = Math.max(heldCount, event.getPointerCount());
			break;
		case MotionEvent.ACTION_MOVE:
			if (heldStart > 0) {
				PointerCoords temp = new PointerCoords();
				float bestX = -1, bestY = -1, bestDist = Float.MAX_VALUE;
				for (int i = 0; i < event.getPointerCount(); i++) {
					event.getPointerCoords(i, temp);
					float dist = ((temp.x - heldLastX) * (temp.x - heldLastX))
							+ ((temp.y - heldLastY) * (temp.y - heldLastY));
					if (dist < bestDist) {
						bestDist = dist;
						bestX = temp.x;
						bestY = temp.y;
					}
				}
				if (bestX > 0) {
					heldLastX = bestX;
					heldLastY = bestY;
					heldDistance += (float) Math.sqrt(bestDist);
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (heldStart > 0) {
				if (onUpdate != null) {
					onUpdate.onSpecialTouch(new SpecialDragEvent(heldStart,
							event.getEventTime() - heldStart, heldCount,
							heldBeginX, heldBeginY, event.getX(), event.getY(), heldDistance));
				}
				heldStart = -1;
			}
			break;
		}
		return false;
	}
}
