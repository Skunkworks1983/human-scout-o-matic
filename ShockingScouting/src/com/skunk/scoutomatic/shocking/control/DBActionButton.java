package com.skunk.scoutomatic.shocking.control;

import com.skunk.scoutomatic.shocking.FieldActivity;
import com.skunk.scoutomatic.util.DatabaseInstance;

public class DBActionButton extends ActionButton {
	private String value;
	private String key;

	public DBActionButton(String name, String key, String value) {
		super(name);
		this.key = key;
		this.value = value;
	}

	@Override
	public void run(FieldActivity act, DatabaseInstance db) {
		value = value.replace("%x", String.valueOf(act.getRobotX()));
		value = value.replace("%y", String.valueOf(act.getRobotY()));
		try {
			if (value.equalsIgnoreCase("true")) {
				db.getData().putBoolean(key, true);
			} else if (value.equalsIgnoreCase("false")) {
				db.getData().putBoolean(key, false);
			} else {
				throw new NumberFormatException("Not a bool.");
			}
			return;
		} catch (Exception e) {
		}

		try {
			db.getData().putInt(key, Integer.valueOf(value));
			return;
		} catch (Exception e) {
		}

		try {
			db.getData().putLong(key, Long.valueOf(value));
			return;
		} catch (Exception e) {
		}

		try {
			db.getData().putFloat(key, Float.valueOf(value));
			return;
		} catch (Exception e) {
		}

		try {
			db.getData().putDouble(key, Double.valueOf(value));
			return;
		} catch (Exception e) {
		}

		db.getData().putString(key, value);
	}
}
