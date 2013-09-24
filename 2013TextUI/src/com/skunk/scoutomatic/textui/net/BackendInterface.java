package com.skunk.scoutomatic.textui.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.skunk.scoutomatic.textui.CollectedData;
import com.skunk.scoutomatic.textui.DataCache;

/**
 * Created on: Sep 22, 2013
 * 
 * @author "Westin Miller"
 * 
 */
public class BackendInterface {
	private static final int SCOUT_ID = 0;
	private static final String EVENT_ID = "2013wase";

	private static final String API_SERVER = "http://machpi.org:9292";
	private static final String API_MATCHES = "/api/scout/register";
	private static final String API_POST_MATCH_DATA = "/api/scout/match";

	private Queue<ScoutableMatch> scoutingQueue = new LinkedBlockingQueue<ScoutableMatch>();
	private final ExecutorService networkPool;

	public BackendInterface() {
		networkPool = Executors.newFixedThreadPool(1);
	}

	private void fetchScoutingQueue() {
		if (scoutingQueue.size() == 0) {
			// Grab it
			try {
				JSONArray thingsToScout = new JSONArray(getJSONFromUrl(
						API_SERVER + API_MATCHES, null, new BasicNameValuePair(
								"event_id", EVENT_ID), new BasicNameValuePair(
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

	public Future<?> pushMatchInformation(final DataCache cache) {
		try {
			final JSONObject matchData = CollectedData.createMatchData(cache);
			return networkPool.submit(new Runnable() {
				public void run() {
					try {
						int mID = matchData.getInt("match_number");
						fetchScoutingQueue();
						while (scoutingQueue.size() > 0) {
							if (scoutingQueue.peek().getMatchID() < mID) {
								scoutingQueue.poll();
							} else {
								break;
							}
						}
						getJSONFromUrl(API_SERVER + API_POST_MATCH_DATA,
								matchData);
					} catch (JSONException e) {
					}
				}
			});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String getJSONFromUrl(String url, JSONObject postData,
			NameValuePair... getParams) {
		InputStream is = null;
		String json = null;
		try {// .setHeader("Accept", "application/json");
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpUriRequest httpPost;
			if (postData == null) {
				StringBuilder fullURL = new StringBuilder();
				fullURL.append(url);
				fullURL.append('?');
				for (int i = 0; i < getParams.length; i++) {
					if (i != 0) {
						fullURL.append('&');
					}
					fullURL.append(URLEncoder.encode(getParams[i].getName(),
							"UTF-8"));
					fullURL.append('=');
					fullURL.append(URLEncoder.encode(getParams[i].getValue(),
							"UTF-8"));
				}
				httpPost = new HttpGet(fullURL.toString());
			} else {
				httpPost = new HttpPost(url);
				StringEntity entity = new StringEntity(postData.toString());
				entity.setContentType("application/json;charset=UTF-8");
				entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json;charset=UTF-8"));
				httpPost.setHeader("Accept", "application/json");
				((HttpPost) httpPost).setEntity(entity);
			}
			Log.d("NET", httpPost.getURI().toString()
					+ (postData != null ? " JSON" : ""));

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
			Log.e("NET", "Buffer Error converting result " + e.toString());
			return null;
		}
		return json;
	}
}
