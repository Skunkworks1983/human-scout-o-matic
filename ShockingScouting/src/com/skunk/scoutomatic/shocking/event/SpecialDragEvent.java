package com.skunk.scoutomatic.shocking.event;

public class SpecialDragEvent extends SpecialTouchEvent {
	private final float startX, startY;
	private final float endX, endY;
	private final long length;
	private final long start;

	public SpecialDragEvent(long start, long length, float startX,
			float startY, float endX, float endY) {
		this.start = start;
		this.length = length;
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
	}

	public long getStart() {
		return start;
	}

	public long getLength() {
		return length;
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
}
