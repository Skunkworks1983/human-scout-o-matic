package com.skunk.scoutomatic.lib.data;

public class Pair {
	private String key;
	private Object value;

	public Pair(String key, Object value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public Object getValue() {
		return value;
	}
}
