package com.skunk.scoutomatic.util;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

public class DatabaseInstance {
	private Bundle singleKeys = new Bundle();
	private List<Action> actions = new ArrayList<Action>();

	public void merge(DatabaseInstance db) {
		actions.addAll(db.actions);
		singleKeys.putAll(db.singleKeys);
	}

	public void addAction(Action a) {
		actions.add(a);
	}

	public Bundle getData() {
		return singleKeys;
	}
}
