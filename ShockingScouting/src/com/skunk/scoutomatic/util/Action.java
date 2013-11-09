package com.skunk.scoutomatic.util;

public class Action {
	private final String type;
	private final String value;
	private final long time;
	private final float fX;
	private final float fY;

	public Action(String type, String value, long time, float fX, float fY) {
		this.type = type;
		this.value = value;
		this.time = time;
		this.fX = fX;
		this.fY = fY;
	}

	public Action(String type, String value, float fX, float fY) {
		this(type, value, System.currentTimeMillis(), fX, fY);
	}

	public long getTime() {
		return time;
	}

	public float getX() {
		return fX;
	}

	public float getY() {
		return fY;
	}

	public String getType() {
		return type;
	}

	public String getValue() {
		return value;
	}
}
