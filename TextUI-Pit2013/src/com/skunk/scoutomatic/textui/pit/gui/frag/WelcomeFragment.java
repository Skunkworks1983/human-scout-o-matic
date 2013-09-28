package com.skunk.scoutomatic.textui.pit.gui.frag;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.skunk.scoutomatic.textui.pit.DataCache;
import com.skunk.scoutomatic.textui.pit.DataKeys;
import com.skunk.scoutomatic.textui.pit.R;

/**
 * Created on: Sep 20, 2013
 * 
 * @author Westin Miller
 */
public class WelcomeFragment extends NamedTabFragment implements TextWatcher {
	private String scoutName;
	private String compID;
	private int robotID;

	private final void registerKeyChange(View v, int id) {
		View vv = v.findViewById(id);
		if (vv != null) {
			((EditText) vv).addTextChangedListener(this);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.welcome_fragment, container, false);
		registerKeyChange(v, R.id.scoutName);
		registerKeyChange(v, R.id.competitionID);
		registerKeyChange(v, R.id.teamID);
		return v;
	}

	@Override
	public String getName() {
		return "Welcome";
	}

	@Override
	public Class<? extends NamedTabFragment> getNext() {
		return null;
	}

	@Override
	public Class<? extends NamedTabFragment> getPrevious() {
		return null;
	}

	@Override
	public void onResume() {
		super.onResume();
		updateContents();
	}

	protected void updateContents() {
		try {
			setTextContents(R.id.scoutName, scoutName);
			setTextContents(R.id.competitionID, compID);
			setTextContents(R.id.teamID,
					robotID > 0 ? Integer.toString(robotID) : "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void afterTextChanged(Editable e) {
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
	}

	@Override
	public void onTextChanged(CharSequence sq, int arg1, int arg2, int arg3) {
		String s = sq.toString();
		if (getTextContents(R.id.scoutName).equalsIgnoreCase(s)) {
			scoutName = s;
		} else if (getTextContents(R.id.competitionID).equalsIgnoreCase(s)) {
			compID = s;
		} else if (getTextContents(R.id.teamID).equalsIgnoreCase(s)) {
			try {
				robotID = Integer.valueOf(s);
			} catch (NumberFormatException ex) {
			}
		}
	}

	@Override
	public void storeInformation(DataCache data) {
		data.putString(DataKeys.ROBOT_SCOUT, scoutName);
		data.putString(DataKeys.ROBOT_COMPETITION, compID);
		data.putInteger(DataKeys.ROBOT_TEAM, robotID);
	}

	@Override
	public void loadInformation(DataCache data) {
		scoutName = data.getString(DataKeys.ROBOT_SCOUT, "");
		compID = data.getString(DataKeys.ROBOT_COMPETITION, "");
		robotID = data.getInteger(DataKeys.ROBOT_TEAM, 0);
	}

	@Override
	public boolean needsKeyboard() {
		return true;
	}
}
