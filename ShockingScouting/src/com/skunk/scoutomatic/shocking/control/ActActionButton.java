package com.skunk.scoutomatic.shocking.control;

import com.skunk.scoutomatic.shocking.FieldActivity;
import com.skunk.scoutomatic.util.Action;
import com.skunk.scoutomatic.util.DatabaseInstance;

public class ActActionButton extends ActionButton {
	private String value;
	private String key;

	public ActActionButton(String name, String key, String value) {
		super(name);
		this.key = key;
		this.value = value;
	}

	@Override
	public void run(FieldActivity act, DatabaseInstance db) {
		db.addAction(new Action(key, value, act.getRobotX(), act.getRobotY()));
	}
}
