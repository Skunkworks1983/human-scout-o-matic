package com.skunk.scoutomatic.textui;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on: Sep 22, 2013
 * 
 * @author "Westin Miller"
 * 
 */
public class ActionCacheUtil {
	public static void forceCount(List<Action> db, ActionType type,
			String result, int count) {
		Action lastAction = null;
		int actionCount = 0;
		List<Action> purge = new ArrayList<Action>();
		for (int i = 0; i < db.size(); i++) {
			Action test = db.get(i);
			if (test.getType() == type
					&& test.getResult().equalsIgnoreCase(result)) {
				lastAction = test;
				actionCount++;
				if (actionCount > count) {
					purge.add(test);
				}
			}
		}
		for (Action a : purge) {
			db.remove(a);
		}
		for (int i = actionCount; i < count; i++) {
			db.add(lastAction != null ? lastAction.clone().setTime(-1)
					: new Action(type, result, 0, 0, -1));
		}
	}

	public static void dropLast(List<Action> db, ActionType type,
			String result, int count) {
		int actionCount = count;
		List<Action> purge = new ArrayList<Action>();
		for (int i = db.size() - 1; i >= 0; i--) {
			Action test = db.get(i);
			if (test.getType() == type
					&& test.getResult().equalsIgnoreCase(result)) {
				if (actionCount > 0) {
					purge.add(test);
					actionCount--;
				} else {
					break;
				}
			}
		}
		for (Action a : purge) {
			db.remove(a);
		}
	}

	public static Action getFirstByActionType(List<Action> db, ActionType type) {
		for (Action a : db) {
			if (a.getType() == type) {
				return a;
			}
		}
		return null;
	}

	public static Action getInsuredActionByType(List<Action> db, ActionType type) {
		for (Action a : db) {
			if (a.getType() == type) {
				return a;
			}
		}
		Action aa = new Action(type, ActionResults.NONE, -1);
		db.add(aa);
		return aa;
	}

	public static void setPositions(List<Action> db, ActionType type,
			String result, float x, float y) {
		for (Action aa : db) {
			if (aa.getType() == type
					&& (result == null || aa.getResult().equalsIgnoreCase(
							result))) {
				aa.setPosition(x, y);
			}
		}
	}

	public static void dropByType(List<Action> db, ActionType type,
			String result) {
		List<Action> purge = new ArrayList<Action>();
		for (Action test : db) {
			if (test.getType() == type
					&& (result == null || test.getResult().equalsIgnoreCase(
							result))) {
				purge.add(test);
			}
		}
		for (Action a : purge) {
			db.remove(a);
		}
	}
}
