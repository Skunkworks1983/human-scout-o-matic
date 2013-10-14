package com.skunk.scoutomatic.textui.gui.frag;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.skunk.scoutomatic.lib.data.DataCache;
import com.skunk.scoutomatic.lib.gui.frag.NamedTabFragmentImpl;
import com.skunk.scoutomatic.textui.DataKeys;
import com.skunk.scoutomatic.textui.R;

/**
 * Created on: Sep 20, 2013
 * 
 * @author Westin Miller
 */
public class WelcomeFragment extends NamedTabFragmentImpl implements TextWatcher {
	private String scoutName;
	private String compID;
	private int robotID;
	private int matchID;

	private final void registerKeyChange(View v, int id) {
		View vv = v.findViewById(id);
		if (vv != null) {
			((EditText) vv).addTextChangedListener(this);
		}
	}
	
	public int getColor() {
		return 0x3300FF00;  // Pale Green
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.welcome_fragment, container, false);
		registerKeyChange(v, R.id.scoutName);
		registerKeyChange(v, R.id.competitionID);
		registerKeyChange(v, R.id.matchID);
		registerKeyChange(v, R.id.teamID);
		return v;
	}

	@Override
	public String getName() {
		return "Welcome";
	}

	@Override
	public Class<? extends NamedTabFragmentImpl> getNext() {
		return AutoLocFragment.class;
	}

	@Override
	public Class<? extends NamedTabFragmentImpl> getPrevious() {
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
			setTextContents(R.id.matchID,
					matchID > 0 ? Integer.toString(matchID) : "");
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
		} else if (getTextContents(R.id.matchID).equalsIgnoreCase(s)) {
			try {
				matchID = Integer.valueOf(s);
			} catch (NumberFormatException ex) {
			}
		}
	}

	@Override
	public void storeInformation(DataCache data) {
		data.putString(DataKeys.MATCH_SCOUT, scoutName);
		data.putString(DataKeys.MATCH_COMPETITION, compID);
		data.putInteger(DataKeys.MATCH_TEAM, robotID);
		data.putInteger(DataKeys.MATCH_NUMBER, matchID);
	}

	@Override
	public void loadInformation(DataCache data) {
		scoutName = data.getString(DataKeys.MATCH_SCOUT, "");
		compID = data.getString(DataKeys.MATCH_COMPETITION, "");
		robotID = data.getInteger(DataKeys.MATCH_TEAM, 0);
		matchID = data.getInteger(DataKeys.MATCH_NUMBER, 0);
	}

	@Override
	public boolean needsKeyboard() {
		return true;
	}
}
