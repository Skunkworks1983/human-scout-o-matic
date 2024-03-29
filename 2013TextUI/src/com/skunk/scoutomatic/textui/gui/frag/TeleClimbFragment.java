package com.skunk.scoutomatic.textui.gui.frag;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.skunk.scoutomatic.lib.data.DataCache;
import com.skunk.scoutomatic.lib.gui.frag.NamedTabFragmentImpl;
import com.skunk.scoutomatic.textui.Action;
import com.skunk.scoutomatic.textui.ActionCacheUtil;
import com.skunk.scoutomatic.textui.ActionResults;
import com.skunk.scoutomatic.textui.ActionType;
import com.skunk.scoutomatic.textui.DataKeys;
import com.skunk.scoutomatic.textui.R;

/**
 * Created on: Sep 20, 2013
 * 
 * @author Westin Miller
 */
public class TeleClimbFragment extends NamedTabFragmentImpl implements
		OnClickListener {
	private int level = 0;
	private boolean attempted = false;

	// Actions
	private List<Action> actionDB = new ArrayList<Action>();

	private final void registerViewWithClickListener(View v, int id) {
		View found = v.findViewById(id);
		if (found != null) {
			found.setOnClickListener(this);
		}
	}

	public int getColor() {
		return 0x33FFFF00;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.tele_climb_fragment, container,
				false);
		registerViewWithClickListener(v, R.id.climbAttempted);
		registerViewWithClickListener(v, R.id.climbLevel1);
		registerViewWithClickListener(v, R.id.climbLevel2);
		registerViewWithClickListener(v, R.id.climbLevel3);
		return v;
	}

	@Override
	public String getName() {
		return "Climbing";
	}

	@Override
	public void onResume() {
		super.onResume();
		updateContents();
	}

	@Override
	public Class<? extends NamedTabFragmentImpl> getNext() {
		return TeleShootLocFragment.class;
	}

	@Override
	public Class<? extends NamedTabFragmentImpl> getPrevious() {
		return TeleScoreFragment.class;
	}

	@Override
	public void onClick(View v) {
		if (v instanceof CheckBox) {
			CheckBox cc = ((CheckBox) v);
			switch (v.getId()) {
			case R.id.climbAttempted:
				attempted = cc.isChecked();
				if (!attempted) {
					level = 0;
				}
				break;
			case R.id.climbLevel1:
				if (!cc.isChecked()) {
					level = 0;
				} else {
					level = 1;
					attempted = true;
				}
				break;
			case R.id.climbLevel2:
				if (!cc.isChecked()) {
					level = 1;
				} else {
					level = 2;
					attempted = true;
				}
				break;
			case R.id.climbLevel3:
				if (!cc.isChecked()) {
					level = 2;
				} else {
					level = 3;
					attempted = true;
				}
				break;
			}
		}
		updateContents();
	}

	protected void updateContents() {
		setState(R.id.climbAttempted, attempted);
		setBackground(R.id.climbAttempted, attempted ? Color.GREEN : Color.RED);
		setState(R.id.climbLevel1, level >= 1);
		setBackground(R.id.climbLevel1, level >= 1 ? Color.GREEN : Color.RED);
		setState(R.id.climbLevel2, level >= 2);
		setBackground(R.id.climbLevel2, level >= 2 ? Color.GREEN : Color.RED);
		setState(R.id.climbLevel3, level >= 3);
		setBackground(R.id.climbLevel3, level >= 3 ? Color.GREEN : Color.RED);
	}

	@Override
	public void storeInformation(DataCache data) {
		data.putInteger(DataKeys.MATCH_TELE_CLIMB_LEVEL, level);
		data.putBoolean(DataKeys.MATCH_TELE_CLIMB_ATTEMPTED, attempted);

		// Actions
		if (attempted) {
			Action aa = ActionCacheUtil.getInsuredActionByType(actionDB,
					ActionType.TELE_CLIMB);
			switch (level) {
			case 1:
				aa.setResult(ActionResults.LEVEL_1);
				break;
			case 2:
				aa.setResult(ActionResults.LEVEL_2);
				break;
			case 3:
				aa.setResult(ActionResults.LEVEL_3);
				break;
			default:
				aa.setResult(ActionResults.LEVEL_0);
				break;
			}
		} else {
			ActionCacheUtil.dropByType(actionDB, ActionType.TELE_CLIMB, null);
		}

		data.putList(DataKeys.MATCH_ACTIONS_KEY, actionDB);
	}

	@Override
	public void loadInformation(DataCache data) {
		level = data.getInteger(DataKeys.MATCH_TELE_CLIMB_LEVEL, 0);
		attempted = data.getBoolean(DataKeys.MATCH_TELE_CLIMB_ATTEMPTED, false);

		// Actions
		actionDB = data.getList(DataKeys.MATCH_ACTIONS_KEY, Action.class);
	}

	@Override
	public boolean needsKeyboard() {
		return false;
	}
}
