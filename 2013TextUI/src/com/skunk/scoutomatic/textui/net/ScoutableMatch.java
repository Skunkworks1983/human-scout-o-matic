package com.skunk.scoutomatic.textui.net;

import org.json.JSONException;
import org.json.JSONObject;

import com.skunk.scoutomatic.textui.Alliance;

/**
 * Created on: Sep 22, 2013
 * 
 * @author "Westin Miller"
 * 
 */
public class ScoutableMatch {
	private JSONObject backend;

	public ScoutableMatch(JSONObject backing) {
		this.backend = backing;
	}

	public int getMatchID() {
		try {
			return backend.getInt("match_number");
		} catch (JSONException e) {
			return -1;
		}
	}

	public int getRobotID() {
		try {
			return backend.getInt("team_number");
		} catch (JSONException e) {
			return -1;
		}
	}

	public Alliance getAlliance() {
		try {
			return Alliance.getAllianceByName(backend.getString("color"));
		} catch (JSONException e) {
			return null;
		}
	}
}
