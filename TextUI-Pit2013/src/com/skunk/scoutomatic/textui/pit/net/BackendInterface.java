package com.skunk.scoutomatic.textui.pit.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.util.Log;

/**
 * Created on: Sep 22, 2013
 * 
 * @author "Westin Miller"
 * 
 */
public class BackendInterface {
	private static final int SCOUT_ID = 0;
	public static final String EVENT_ID = "2013wase";

	private static final String API_SERVER = "http://machpi.org:9292";

	private final ExecutorService networkPool;

	public BackendInterface() {
		networkPool = Executors.newFixedThreadPool(1);
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
