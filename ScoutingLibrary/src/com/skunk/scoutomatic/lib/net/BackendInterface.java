package com.skunk.scoutomatic.lib.net;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created on: Sep 22, 2013
 * 
 * @author "Westin Miller"
 * 
 */
public abstract class BackendInterface implements Runnable {
	private ScheduledExecutorService networkPool;

	private List<NetworkAction> deferredNetworkActions = Collections
			.synchronizedList(new ArrayList<NetworkAction>());
	private ScheduledFuture<?> defferedProcessor;
	private Map<String, String> cachedResponseMap = new HashMap<String, String>();

	public BackendInterface() {
		networkPool = Executors.newScheduledThreadPool(1);
		defferedProcessor = networkPool.scheduleAtFixedRate(this,
				getPollSpeed(), getPollSpeed(), TimeUnit.SECONDS);
	}

	protected abstract Context getApplicationContext();

	protected final String getAPIServer() {
		return PreferenceManager.getDefaultSharedPreferences(
				getApplicationContext()).getString(
				NetworkSettingsKeys.BACKEND_URI,
				NetworkSettingsKeys.DEFAULT_BACKEND_URI);
	}

	protected final String getEventID() {
		return PreferenceManager.getDefaultSharedPreferences(
				getApplicationContext()).getString(
				NetworkSettingsKeys.EVENT_ID,
				NetworkSettingsKeys.DEFAULT_EVENT_ID);
	}

	protected final long getPollSpeed() {
		try {
			return Long
					.valueOf(PreferenceManager
							.getDefaultSharedPreferences(
									getApplicationContext())
							.getString(
									NetworkSettingsKeys.BACKEND_POLL_SPEED,
									String.valueOf(NetworkSettingsKeys.DEFAULT_BACKEND_POLL_SPEED)));
		} catch (Exception e) {
		}
		return NetworkSettingsKeys.DEFAULT_BACKEND_POLL_SPEED;
	}

	protected final int getTabletID() {
		try {
			return Integer
					.valueOf(PreferenceManager
							.getDefaultSharedPreferences(
									getApplicationContext())
							.getString(
									NetworkSettingsKeys.TABLET_ID,
									String.valueOf(NetworkSettingsKeys.DEFAULT_TABLET_ID)));
		} catch (Exception e) {
		}
		return NetworkSettingsKeys.DEFAULT_TABLET_ID;
	}

	protected Future<?> runUninsuredAction(final NetworkAction action) {
		return networkPool.submit(new Runnable() {
			@Override
			public void run() {
				doNetworkAction(action);
			}
		});
	}

	protected void runInsuredAction(NetworkAction action) {
		deferredNetworkActions.add(action);
		networkPool.submit(this);
	}

	private final boolean doNetworkAction(NetworkAction action) {
		if (action.doesCache() && cachedResponseMap.containsKey(action.getID())) {
			if (action.callWithCache(cachedResponseMap.get(action.getID()))) {
				return true;
			}
		}
		String returnVal = action.tryNetworkAction();
		if (returnVal != null) {
			if (action.doesCache()) {
				cachedResponseMap.put(action.getID(), returnVal);
			}
			return true;
		}
		return false;
	}

	@Override
	public void run() {
		try {
			// Try network actions; if we manage remove it
			Iterator<NetworkAction> iterator = deferredNetworkActions
					.iterator();

			while (iterator.hasNext()) {
				if (doNetworkAction(iterator.next())) {
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
		defferedProcessor = networkPool.scheduleAtFixedRate(this,
				getPollSpeed(), getPollSpeed(), TimeUnit.SECONDS);
	}
}
