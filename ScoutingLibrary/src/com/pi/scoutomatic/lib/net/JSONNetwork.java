package com.pi.scoutomatic.lib.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
 * Created on: Oct 9, 2013
 * 
 * @author "Westin Miller"
 * 
 */
public class JSONNetwork {
	public static String getJSONFromUrl(String url, JSONObject postData,
			NameValuePair... getParams) throws Exception {
		InputStream is = null;
		String json = null;
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpUriRequest httpPost;

			StringBuilder fullURL = new StringBuilder();
			fullURL.append(url);
			if (getParams.length > 0) {
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
			}
			if (postData == null) {
				httpPost = new HttpGet(fullURL.toString());
			} else {
				httpPost = new HttpPost(fullURL.toString());
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
			throw e;
		} catch (ClientProtocolException e) {
			throw e;
		} catch (IOException e) {
			throw e;
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
