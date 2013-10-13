package com.skunk.scoutomatic.textui;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pi.scoutomatic.lib.data.DataCache;

/**
 * Created on: Sep 22, 2013
 * 
 * @author "Westin Miller"
 * 
 */
public class CollectedData {
	public static JSONObject createMatchData(DataCache heap)
			throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("event_id", heap.getString(DataKeys.MATCH_COMPETITION, null));
		obj.put("team_number", heap.getInteger(DataKeys.MATCH_TEAM, -1));
		obj.put("match_number", heap.getInteger(DataKeys.MATCH_NUMBER, -1));
		JSONArray array = new JSONArray();
		List<Action> actions = heap.getList(DataKeys.MATCH_ACTIONS_KEY,
				Action.class);
		for (Action a : actions) {
			array.put(new JSONObject(a.toString()));
		}
		obj.put("actions", array);
		return obj;
	}
}
