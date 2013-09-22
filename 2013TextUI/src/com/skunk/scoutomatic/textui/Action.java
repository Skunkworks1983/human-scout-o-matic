package com.skunk.scoutomatic.textui;

/**
 * Created on: Sep 21, 2013
 * 
 * @author "Westin Miller"
 * 
 */
public class Action {
	private long time;
	private ActionType type;
	private String result;
	private float x, y;

	public Action(ActionType type, String result, float x, float y, long time) {
		this.type = type;
		this.result = result;
		this.x = x;
		this.y = y;
		this.time = time;
	}

	public Action(ActionType type, String result, long time) {
		this(type, result, 0, 0, time);
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
}
