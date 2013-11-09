package com.skunk.scoutomatic.shocking.event;

public class SpecialDragEvent extends SpecialTouchEvent {
	private final float startX, startY;
	private final float endX, endY;
	private final long time;
	private final long start;
	private final int touchCount;

	public SpecialDragEvent(long start, long time, int touchCount,
			float startX, float startY, float endX, float endY) {
		this.start = start;
		this.time = time;
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.touchCount = touchCount;
	}

	public long getStart() {
		return start;
	}

	public long getTime() {
		return time;
	}

	public float getEndY() {
		return endY;
	}

	public float getEndX() {
		return endX;
	}

	public float getStartX() {
		return startX;
	}

	public float getStartY() {
		return startY;
	}

	public int getTouchCount() {
		return touchCount;
	}

	public float getDistance() {
		float dX = startX - endX;
		float dY = startY - endY;
		return (float) Math.sqrt((dX * dX) + (dY * dY));
	}

	public String toString() {
		return "Drag[dist=" + getDistance() + ",time=" + getTime() + ",count="
				+ getTouchCount() + "]";
	}
}
