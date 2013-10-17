package com.skunk.scoutomatic.lib.net;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.util.Log;

public class NetworkAction {
	private String id;
	private JSONObject postData;
	private FutureProcessor<String> futureProcessor;
	private String url;
	private NameValuePair[] varsGET;
	private int tries = 0;
	private String error = "";
	private boolean doesCache;

	public NetworkAction(String id, String url, JSONObject postData,
			FutureProcessor<String> callback, NameValuePair[] getVars) {
		this.id = id;
		this.url = url;
		this.postData = postData;
		this.futureProcessor = callback;
		this.varsGET = getVars;
		this.doesCache = false;
	}

	public NetworkAction setCaching(boolean doesCache) {
		this.doesCache = true;
		return this;
	}

	public boolean doesCache() {
		return doesCache;
	}

	public String getError() {
		return error;
	}

	public String getID() {
		return id;
	}

	public int getTries() {
		return tries;
	}

	String tryNetworkAction() {
		try {
			tries++;
			String s = JSONNetwork.getJSONFromUrl(url, postData, varsGET);
			if (s == null) {
				return null;
			}
			if (futureProcessor != null) {
				futureProcessor.run(s);
			}
			error = "";
			return s;
		} catch (Exception e) {
			error = "Error: " + e.getMessage();
			Log.e("NET", e.toString());
			return null;
		}
	}

	boolean callWithCache(String s) {
		try {
			if (futureProcessor != null) {
				futureProcessor.run(s);
			}
			return true;
		} catch (Exception e) {
			error = "Cache Error: " + e.getMessage();
			Log.e("NET", "Cache: " + e.toString());
			return false;
		}
	}
}