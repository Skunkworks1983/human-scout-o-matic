package com.skunk.scoutomatic.lib.net;

/**
 * Created on: Sep 22, 2013
 * 
 * @author "Westin Miller"
 * 
 */
public interface FutureProcessor<T> {
	public void run(T o);
}
