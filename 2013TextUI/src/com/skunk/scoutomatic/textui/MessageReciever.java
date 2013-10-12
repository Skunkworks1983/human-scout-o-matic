package com.skunk.scoutomatic.textui;

/**
 * Created on: Oct 9, 2013
 * 
 * @author "Westin Miller"
 * 
 */
public interface MessageReciever {
	public void onMessage(Class<?> src, String message);
}
