package com.skunk.scoutomatic.textui.pit.net;

import java.util.Queue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import android.content.Context;
import android.util.Log;

import com.skunk.scoutomatic.lib.data.DataCache;
import com.skunk.scoutomatic.lib.net.BackendInterface;
import com.skunk.scoutomatic.lib.net.FutureProcessor;
import com.skunk.scoutomatic.lib.net.NetworkAction;
import com.skunk.scoutomatic.textui.pit.gui.PitMainActivity;

/**
 * Created on: Sep 22, 2013
 * 
 * @author "Westin Miller"
 * 
 */
public class PitBackendInterface extends BackendInterface {
	private static final String API_PIT_ROBOTS = "/api/scout/pit/robots";
	private Queue<ScoutableRobot> scoutingQueue = new LinkedBlockingQueue<ScoutableRobot>();
	private PitMainActivity activity;

	public PitBackendInterface(PitMainActivity act) {
		super();
		this.activity = act;
	}

	public Future<?> pollScoutingQueue(
			final FutureProcessor<ScoutableRobot> mm, final boolean peek) {
		return super.runUninsuredAction(new NetworkAction("GrabScoutingQueue",
				getAPIServer() + API_PIT_ROBOTS, null,
				new FutureProcessor<String>() {
					@Override
					public void run(String o) {
						if (scoutingQueue.size() == 0) {
							try {
								JSONArray thingsToScout = new JSONArray(o);
								for (int i = 0; i < thingsToScout.length(); i++) {
									scoutingQueue.add(new ScoutableRobot(
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

	public void pushRobotInformation(final DataCache cache) {
		// TODO
	}

	@Override
	protected Context getApplicationContext() {
		return activity.getApplicationContext();
	}
}
