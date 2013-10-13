package com.pi.scoutomatic.lib.data;


/**
 * Created on: Sep 22, 2013
 * 
 * @author "Westin Miller"
 * 
 */
public enum Alliance {
	BLUE, RED;
	public static Alliance getAllianceByName(String s) {
		for (Alliance a : values()) {
			if (a.name().equalsIgnoreCase(s)) {
				return a;
			}
		}
		return null;
	}
}
