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
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.preference.PreferenceManager;
import android.util.Log;

import com.skunk.scoutomatic.textui.CollectedData;
import com.skunk.scoutomatic.textui.DataCache;
import com.skunk.scoutomatic.textui.DataKeys;
import com.skunk.scoutomatic.textui.SettingsKeys;
import com.skunk.scoutomatic.textui.gui.MainActivity;

/**
 * Created on: Sep 22, 2013
 * 
 * @author "Westin Miller"
 * 
 */
public class BackendInterface implements Runnable {
	private static final String API_MATCHES = "/api/scout/register";
	private static final String API_POST_MATCH_DATA = "/api/scout/match";

	private static final long DEFAULT_NETWORK_TRY_SPEED = 30; // 30
																// seconds
	private static final String DEFAULT_API_SERVER = "http://machpi.org:9292";
	private static final int DEFAULT_SCOUT_ID = 0;
	public static final String DEFAULT_EVENT_ID = "2013wase";

	public static class NetworkAction {
		private String id;
		private JSONObject postData;
		private FutureProcessor<String> futureProcessor;
		private String url;
		private NameValuePair[] varsGET;
		private int tries = 0;
		private String error = "";

		private NetworkAction(String id, String url, JSONObject postData,
				FutureProcessor<String> callback, NameValuePair[] getVars) {
			this.id = id;
			this.url = url;
			this.postData = postData;
			this.futureProcessor = callback;
			this.varsGET = getVars;
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

		private boolean tryNetworkAction() {
			try {
				tries++;
				String s = JSONNetwork.getJSONFromUrl(url, postData, varsGET);
				if (s == null) {
					return false;
				}
				if (futureProcessor != null) {
					futureProcessor.run(s);
				}
				error = "";
				return true;
			} catch (Exception e) {
				error = "Error: " + e.getMessage();
				Log.e("NET", e.toString());
				return false;
			}
		}
	}

	private Queue<ScoutableMatch> scoutingQueue = new LinkedBlockingQueue<ScoutableMatch>();
	private ScheduledExecutorService networkPool;
	private final MainActivity activity;

	private List<NetworkAction> deferredNetworkActions = Collections
			.synchronizedList(new ArrayList<NetworkAction>());
	private ScheduledFuture<?> defferedProcessor;

	public BackendInterface(MainActivity act) {
		networkPool = Executors.newScheduledThreadPool(1);
		activity = act;
		defferedProcessor = networkPool.scheduleAtFixedRate(this, activity
				.getLongPreference(SettingsKeys.BACKEND_POLL_SPEED,
						DEFAULT_NETWORK_TRY_SPEED), activity.getLongPreference(
				SettingsKeys.BACKEND_POLL_SPEED, DEFAULT_NETWORK_TRY_SPEED),
				TimeUnit.SECONDS);
	}

	private final String getAPIServer() {
		return PreferenceManager.getDefaultSharedPreferences(
				activity.getApplicationContext()).getString(
				SettingsKeys.BACKEND_URI, DEFAULT_API_SERVER);
	}

	private final String getEventID() {
		return PreferenceManager.getDefaultSharedPreferences(
				activity.getApplicationContext()).getString(
				SettingsKeys.PREF_COMPETITION_ID, DEFAULT_EVENT_ID);
	}

	private final int getScoutID() {
		return (int) activity.getLongPreference(SettingsKeys.PREF_TABLET_ID,
				DEFAULT_SCOUT_ID);
	}

	private void fetchScoutingQueue() {
		if (scoutingQueue.size() == 0) {
			// Grab it
			try {
				JSONArray thingsToScout = new JSONArray(
						JSONNetwork
								.getJSONFromUrl(getAPIServer() + API_MATCHES,
										null, new BasicNameValuePair(
												"event_id", getEventID()),
										new BasicNameValuePair("scout_id",
												String.valueOf(getScoutID()))));
				for (int i = 0; i < thingsToScout.length(); i++) {
					scoutingQueue.add(new ScoutableMatch(thingsToScout
							.getJSONObject(i)));
				}
			} catch (Exception e) {
				Log.e("NET", "Couldn't get scouting queue.  (" + e.getMessage()
						+ ")");
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
			NetworkAction action = new NetworkAction("Robot "
					+ cache.getInteger(DataKeys.MATCH_TEAM, -1) + " in match "
					+ cache.getInteger(DataKeys.MATCH_NUMBER, -1) + " of "
					+ cache.getString(DataKeys.MATCH_COMPETITION, "null"),
					getAPIServer() + API_POST_MATCH_DATA, matchData,
					new FutureProcessor<String>() {
						@Override
						public void run(String o) {
							int mID;
							try {
								mID = matchData.getInt("match_number");

								// Message!
								activity.onMessage(BackendInterface.class,
										"Match " + mID + "'s data is off!");

								fetchScoutingQueue();
								while (scoutingQueue.size() > 0) {
									if (scoutingQueue.peek().getMatchID() < mID) {
										scoutingQueue.poll();
									} else {
										break;
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
								Log.e("NET", e.toString());
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
		try {
			// Try network actions; if we manage remove it
			Iterator<NetworkAction> iterator = deferredNetworkActions
					.iterator();

			while (iterator.hasNext()) {
				NetworkAction action = iterator.next();
				boolean returnVal = action.tryNetworkAction();
				if (returnVal) {
					iterator.remove();
				}
			}

		} catch (RuntimeException e) {
		}
	}

	public List<NetworkAction> getBacklog() {
		return deferredNetworkActions;
	}

	public void shutdownQueue(long wait) {
		if (defferedProcessor != null && !defferedProcessor.isCancelled()) {
			defferedProcessor.cancel(false);
			if (wait > 0) {
				try {
					defferedProcessor.get(wait, TimeUnit.MILLISECONDS);
				} catch (Exception e) {
				}
			}
		}
	}

	public void beginQueue() {
		shutdownQueue(2000L);
		defferedProcessor = networkPool.scheduleAtFixedRate(this, activity
				.getLongPreference(SettingsKeys.BACKEND_POLL_SPEED,
						DEFAULT_NETWORK_TRY_SPEED), activity.getLongPreference(
				SettingsKeys.BACKEND_POLL_SPEED, DEFAULT_NETWORK_TRY_SPEED),
				TimeUnit.SECONDS);
	}
}
