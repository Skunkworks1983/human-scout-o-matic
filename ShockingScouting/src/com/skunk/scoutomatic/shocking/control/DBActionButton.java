package com.skunk.scoutomatic.shocking.control;

import android.os.Bundle;

import com.skunk.scoutomatic.shocking.FieldActivity;

public class DBActionButton extends ActionButton {
	private String value;
	private String key;

	public DBActionButton(String name, String key, String value) {
		super(name);
		this.key = key;
		this.value = value;
	}

	@Override
	public void run(FieldActivity act) {
		value = value.replace("%x", String.valueOf(act.getRobotX()));
		value = value.replace("%y", String.valueOf(act.getRobotY()));
		Bundle db = act.getMainDatabase();
		try {
			if (value.equalsIgnoreCase("true")) {
				db.putBoolean(key, true);
			} else if (value.equalsIgnoreCase("false")) {
				db.putBoolean(key, false);
			} else {
				throw new NumberFormatException("Not a bool.");
			}
			return;
		} catch (Exception e) {
		}

		try {
			db.putInt(key, Integer.valueOf(value));
			return;
		} catch (Exception e) {
		}

		try {
			db.putLong(key, Long.valueOf(value));
			return;
		} catch (Exception e) {
		}

		try {
			db.putFloat(key, Float.valueOf(value));
			return;
		} catch (Exception e) {
		}

		try {
			db.putDouble(key, Double.valueOf(value));
			return;
		} catch (Exception e) {
		}

		db.putString(key, value);
	}
}
