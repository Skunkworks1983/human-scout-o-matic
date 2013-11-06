package com.skunk.scoutomatic.shocking.event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.skunk.scoutomatic.util.ObjectFilter;

public class EventFactory implements OnTouchListener {
	private List<SpecialTouchEvent> eventQueue = new ArrayList<SpecialTouchEvent>();
	private long heldStart = -1;
	private float heldBeginX, heldBeginY;

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
				synchronized (eventQueue) {
					eventQueue.add(new SpecialDragEvent(heldStart, event
							.getEventTime() - heldStart, heldBeginX,
							heldBeginY, event.getX(), event.getY()));
				}
				heldStart = -1;
			}
			break;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public <T extends SpecialTouchEvent> T popEvent(Class<T> clazz,
			ObjectFilter<T> filter) {
		synchronized (eventQueue) {
			Iterator<SpecialTouchEvent> itr = eventQueue.iterator();
			while (itr.hasNext()) {
				SpecialTouchEvent evt = itr.next();
				if (evt != null && clazz.isInstance(evt)
						&& (filter == null || filter.accept((T) evt))) {
					itr.remove();
					return (T) evt;
				}
			}
		}
		return null;
	}
}
