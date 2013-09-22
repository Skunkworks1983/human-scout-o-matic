package com.skunk.scoutomatic.textui.gui.frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.skunk.scoutomatic.textui.DataCache;
import com.skunk.scoutomatic.textui.DataKeys;
import com.skunk.scoutomatic.textui.R;

/**
 * Created on: Sep 20, 2013
 * 
 * @author Westin Miller
 */
public class TeleClimbFragment extends NamedTabFragment implements
		OnClickListener {
	private int level = 0;
	private boolean attempted = false;

	private final void registerViewWithClickListener(View v, int id) {
		View found = v.findViewById(id);
		if (found != null) {
			found.setOnClickListener(this);
		}
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
		updateLevel();
	}

	@Override
	public Class<? extends NamedTabFragment> getNext() {
		return TeleShootLocFragment.class;
	}

	@Override
	public Class<? extends NamedTabFragment> getPrevious() {
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
		updateLevel();
	}

	private void updateLevel() {
		setState(R.id.climbAttempted, attempted);
		setState(R.id.climbLevel1, level >= 1);
		setState(R.id.climbLevel2, level >= 2);
		setState(R.id.climbLevel3, level >= 3);
	}

	@Override
	public void storeInformation(DataCache data) {
		data.putInteger(DataKeys.MATCH_TELE_CLIMB_LEVEL, level);
		data.putBoolean(DataKeys.MATCH_TELE_CLIMB_ATTEMPTED, attempted);
	}

	@Override
	public void loadInformation(DataCache data) {
		level = data.getInteger(DataKeys.MATCH_TELE_CLIMB_LEVEL, 0);
		attempted = data.getBoolean(DataKeys.MATCH_TELE_CLIMB_ATTEMPTED, false);
	}
}
