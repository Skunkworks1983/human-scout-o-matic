package com.skunk.scoutomatic.shocking.event;

public class SpecialDragEvent extends SpecialTouchEvent {
	private final float startX, startY;
	private final float endX, endY;
	private final long time;
	private final long start;

	public SpecialDragEvent(long start, long time, float startX, float startY,
			float endX, float endY) {
		this.start = start;
		this.time = time;
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
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

	public float getDistance() {
		float dX = startX - endX;
		float dY = startY - endY;
		return (float) Math.sqrt((dX * dX) + (dY * dY));
	}

	public String toString() {
		return "Drag[dist=" + getDistance() + ",time=" + getTime() + "]";
	}
}
