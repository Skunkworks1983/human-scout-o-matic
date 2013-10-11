package com.skunk.scoutomatic.textui;

import android.util.Log;

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
		if (result == null || result.length() == 0) {
			result = "null";
			Log.w("DB", "Action with null result was created!");
		} else if (result.equals("0.0")) {
			result = "0.01"; // Safety
			Log.w("DB", "Action with zero result was created!");
		} else if (result.equals("0")) {
			result = "0.01"; // Safety
			Log.w("DB", "Action with zero result was created!");
		}
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
		return "{\"time\":\"" + time + "\",\"action\":\"" + type
				+ "\",\"result\":\"" + result + "\",\"x\":\"" + x
				+ "\",\"y\":\"" + y + "\"}";
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
