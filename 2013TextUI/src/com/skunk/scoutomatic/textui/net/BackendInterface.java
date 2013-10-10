package com.skunk.scoutomatic.textui.net;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.skunk.scoutomatic.textui.CollectedData;
import com.skunk.scoutomatic.textui.DataCache;

/**
 * Created on: Sep 22, 2013
 * 
 * @author "Westin Miller"
 * 
 */
public class BackendInterface implements Runnable {
	private static final long NETWORK_TRY_SPEED_MILLIS = 30000; // 30 seconds
	private static final int SCOUT_ID = 0;
	public static final String EVENT_ID = "2013wase";

	private static final String API_SERVER = "http://machpi.org:9292";
	private static final String API_MATCHES = "/api/scout/register";
	private static final String API_POST_MATCH_DATA = "/api/scout/match";

	private static class NetworkAction {
		private JSONObject postData;
		private FutureProcessor<String> futureProcessor;
		private String url;
		private NameValuePair[] varsGET;
		private int tries = 0;

		public NetworkAction(String url, JSONObject postData,
				FutureProcessor<String> callback, NameValuePair[] getVars) {
			this.url = url;
			this.postData = postData;
			this.futureProcessor = callback;
			this.varsGET = getVars;
		}

		private boolean tryNetworkAction() {
			tries++;
			String s = JSONNetwork.getJSONFromUrl(url, postData, varsGET);
			if (s == null) {
				return false;
			}
			if (futureProcessor != null) {
				futureProcessor.run(s);
			}
			return true;
		}
	}

	private Queue<ScoutableMatch> scoutingQueue = new LinkedBlockingQueue<ScoutableMatch>();
	private final ScheduledExecutorService networkPool;

	private List<NetworkAction> deferredNetworkActions = Collections
			.synchronizedList(new ArrayList<NetworkAction>());

	public BackendInterface() {
		networkPool = Executors.newScheduledThreadPool(1);
		networkPool.scheduleAtFixedRate(this, NETWORK_TRY_SPEED_MILLIS,
				NETWORK_TRY_SPEED_MILLIS, TimeUnit.MILLISECONDS);
	}

	private void fetchScoutingQueue() {
		if (scoutingQueue.size() == 0) {
			// Grab it
			try {
				JSONArray thingsToScout = new JSONArray(
						JSONNetwork.getJSONFromUrl(API_SERVER + API_MATCHES,
								null, new BasicNameValuePair("event_id",
										EVENT_ID), new BasicNameValuePair(
										"scout_id", String.valueOf(SCOUT_ID))));
				for (int i = 0; i < thingsToScout.length(); i++) {
					scoutingQueue.add(new ScoutableMatch(thingsToScout
							.getJSONObject(i)));
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public Future<?> peekScoutingQueue(final FutureProcessor<ScoutableMatch> mm) {
		return networkPool.submit(new Runnable() {
			public void run() {
				fetchScoutingQueue();
				mm.run(scoutingQueue.peek());
			}
		});
	}

	public Future<?> popScoutingQueue(final FutureProcessor<ScoutableMatch> mm) {
		return networkPool.submit(new Runnable() {
			public void run() {
				fetchScoutingQueue();
				mm.run(scoutingQueue.poll());
			}
		});
	}

	public void pushMatchInformation(final DataCache cache) {
		try {
			final JSONObject matchData = CollectedData.createMatchData(cache);
			NetworkAction action = new NetworkAction(API_SERVER
					+ API_POST_MATCH_DATA, matchData,
					new FutureProcessor<String>() {
						@Override
						public void run(String o) {
							int mID;
							try {
								mID = matchData.getInt("match_number");
								fetchScoutingQueue();
								while (scoutingQueue.size() > 0) {
									if (scoutingQueue.peek().getMatchID() < mID) {
										scoutingQueue.poll();
									} else {
										break;
									}
								}
							} catch (JSONException e) {
							}
						}
					}, new NameValuePair[0]);
			deferredNetworkActions.add(action);
			networkPool.submit(this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void run() {
		// Try network actions; if we manage remove it
		Iterator<NetworkAction> iterator = deferredNetworkActions.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().tryNetworkAction()) {
				iterator.remove();
			}
		}
	}
}
