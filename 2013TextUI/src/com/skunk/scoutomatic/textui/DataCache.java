package com.skunk.scoutomatic.textui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on: Sep 21, 2013
 * 
 * @author "Westin Miller"
 * 
 */
public class DataCache {
	private Map<String, Object> data = new HashMap<String, Object>();

	public DataCache(Map<String, Object> database) {
		this.data = database;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void putBoolean(String key, boolean value) {
		data.put(key, Boolean.valueOf(value));
	}

	public void putFloat(String key, float value) {
		data.put(key, Float.valueOf(value));
	}

	public void putInteger(String key, int value) {
		data.put(key, Integer.valueOf(value));
	}

	public void putLong(String key, long value) {
		data.put(key, Long.valueOf(value));
	}

	public void putString(String key, String value) {
		data.put(key, value);
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		Object o = data.get(key);
		if (o != null && o instanceof Boolean) {
			return ((Boolean) o).booleanValue();
		}
		return defaultValue;
	}

	public float getFloat(String key, float defaultValue) {
		Object o = data.get(key);
		if (o != null && o instanceof Number) {
			return ((Number) o).floatValue();
		}
		return defaultValue;
	}

	public int getInteger(String key, int defaultValue) {
		Object o = data.get(key);
		if (o != null && o instanceof Number) {
			return ((Number) o).intValue();
		}
		return defaultValue;
	}

	public long getLong(String key, long defaultValue) {
		Object o = data.get(key);
		if (o != null && o instanceof Number) {
			return ((Number) o).longValue();
		}
		return defaultValue;
	}

	public String getString(String key, String defaultValue) {
		Object o = data.get(key);
		if (o != null) {
			return o.toString();
		}
		return defaultValue;
	}

	@SuppressWarnings("unchecked")
	/**
	 * 
	 * @param key The String to fetch
	 * @param clazz Used to ID the type
	 * @return
	 */
	public <T> List<T> getList(String key, Class<T> clazz) {
		Object o = data.get(key);
		ArrayList<T> lst = new ArrayList<T>(0);
		if (o instanceof List) {
			List<?> generic = (List<?>) o;
			lst.ensureCapacity(generic.size());
			for (int i = 0; i < generic.size(); i++) {
				lst.add((T) generic.get(i));
			}
			return lst;
		}
		return lst;
	}

	public void putList(String key, List<?> lst) {
		data.put(key, lst);
	}
}
