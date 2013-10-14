package com.skunk.scoutomatic.textui.net;

import java.util.Queue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.skunk.scoutomatic.lib.data.DataCache;
import com.skunk.scoutomatic.lib.net.BackendInterface;
import com.skunk.scoutomatic.lib.net.FutureProcessor;
import com.skunk.scoutomatic.lib.net.NetworkAction;
import com.skunk.scoutomatic.textui.CollectedData;
import com.skunk.scoutomatic.textui.DataKeys;
import com.skunk.scoutomatic.textui.gui.Text2013Activity;

/**
 * Created on: Sep 22, 2013
 * 
 * @author "Westin Miller"
 * 
 */
public class Text2013BackendInterface extends BackendInterface {
	private static final String API_MATCHES = "/api/scout/register";
	private static final String API_POST_MATCH_DATA = "/api/scout/match";
	private Queue<ScoutableMatch> scoutingQueue = new LinkedBlockingQueue<ScoutableMatch>();
	private Text2013Activity activity;

	public Text2013BackendInterface(Text2013Activity act) {
		super();
		this.activity = act;
	}

	public Future<?> pollScoutingQueue(
			final FutureProcessor<ScoutableMatch> mm, final boolean peek) {
		return super.runUninsuredAction(new NetworkAction("GrabScoutingQueue",
				getAPIServer() + API_MATCHES, null,
				new FutureProcessor<String>() {
					@Override
					public void run(String o) {
						if (scoutingQueue.size() == 0) {
							try {
								JSONArray thingsToScout = new JSONArray(o);
								for (int i = 0; i < thingsToScout.length(); i++) {
									scoutingQueue.add(new ScoutableMatch(
											thingsToScout.getJSONObject(i)));
								}
							} catch (Exception e) {
								Log.e("NET", "Couldn't get scouting queue.  ("
										+ e.getMessage() + ")");
							}
						}
						if (scoutingQueue.size() > 0 && mm != null) {
							mm.run(peek ? scoutingQueue.peek() : scoutingQueue
									.poll());
						}
					}
				}, new NameValuePair[] {
						new BasicNameValuePair("event_id", getEventID()),
						new BasicNameValuePair("scout_id", String
								.valueOf(getTabletID())) }).setCaching(true));
	}

	public void pushMatchInformation(final DataCache cache) {
		try {
			final JSONObject matchData = CollectedData.createMatchData(cache);
			final int mID = matchData.getInt("match_number");
			NetworkAction action = new NetworkAction("Robot "
					+ cache.getInteger(DataKeys.MATCH_TEAM, -1) + " in match "
					+ cache.getInteger(DataKeys.MATCH_NUMBER, -1) + " of "
					+ cache.getString(DataKeys.MATCH_COMPETITION, "null"),
					getAPIServer() + API_POST_MATCH_DATA, matchData,
					new FutureProcessor<String>() {
						@Override
						public void run(String o) {
							// Message!
							activity.onMessage(BackendInterface.class, "Match "
									+ mID + "'s data is off!");

							while (scoutingQueue.size() > 0) {
								if (scoutingQueue.peek().getMatchID() < mID) {
									scoutingQueue.poll();
								} else {
									break;
								}
							}
						}
					}, new NameValuePair[0]);
			runInsuredAction(action);
		} catch (Exception e) {
			Log.e("DATA",
					"Unable to create JSON data for match: \"" + e.getMessage()
							+ "\"");
		}
	}

	@Override
	protected Context getApplicationContext() {
		return activity.getApplicationContext();
	}
}
