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

	public Action setPosition(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public ActionType getType() {
		return type;
	}

	public boolean equals(Object o) {
		if (o instanceof Action) {
			Action a = (Action) o;
			return a.type == type && a.result.equalsIgnoreCase(result)
					&& a.time == time && a.y == y && a.x == x;
		}
		return false;
	}

	public Action clone() {
		return new Action(type, result, x, y, time);
	}

	public String toString() {
		return "Action[" + type.name() + ":" + result + "@" + x + "," + y + "@"
				+ time;
	}

	public Action setTime(int i) {
		this.time = i;
		return this;
	}

	public String getResult() {
		return result;
	}

	public Action setResult(String res) {
		this.result = res;
		return this;
	}
}
