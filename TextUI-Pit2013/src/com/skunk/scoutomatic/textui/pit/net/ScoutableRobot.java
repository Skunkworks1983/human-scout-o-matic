package com.skunk.scoutomatic.textui.pit.net;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created on: Oct 13, 2013
 * 
 * @author "Westin Miller"
 * 
 */
public class ScoutableRobot {
	private JSONObject backend;

	public ScoutableRobot(JSONObject backing) {
		this.backend = backing;
	}

	public int getRobotID() {
		try {
			return backend.getInt("team_number");
		} catch (JSONException e) {
			return -1;
		}
	}
}
