package com.skunk.scoutomatic.textui.gui.frag;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.skunk.scoutomatic.textui.R;

/**
 * Created on: Sep 20, 2013
 * 
 * @author Westin Miller
 */
public class WelcomeFragment extends NamedTabFragment implements TextWatcher {
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
	public Class<? extends NamedTabFragment> getNext() {
		return AutoLocFragment.class;
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

	private void updateContents() {
		setTextContents(R.id.scoutName, scoutName);
		setTextContents(R.id.competitionID, compID);
		setTextContents(R.id.matchID, matchID > 0 ? Integer.toString(matchID)
				: "");
		setTextContents(R.id.teamID, robotID > 0 ? Integer.toString(robotID)
				: "");
	}

	@Override
	public void afterTextChanged(Editable e) {
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// Just update cache
		scoutName = getTextContents(R.id.scoutName);
		compID = getTextContents(R.id.competitionID);
		try {
			robotID = Integer.valueOf(getTextContents(R.id.teamID));
		} catch (NumberFormatException ex) {
		}
		try {
			matchID = Integer.valueOf(getTextContents(R.id.matchID));
		} catch (NumberFormatException ex) {
		}
	}
}
