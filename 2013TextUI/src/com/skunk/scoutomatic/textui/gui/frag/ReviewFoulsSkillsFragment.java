package com.skunk.scoutomatic.textui.gui.frag;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;

import com.skunk.scoutomatic.textui.DataCache;
import com.skunk.scoutomatic.textui.DataKeys;
import com.skunk.scoutomatic.textui.R;

/**
 * Created on: Sep 20, 2013
 * 
 * @author Westin Miller
 */
public class ReviewFoulsSkillsFragment extends NamedTabFragment implements
		OnClickListener, TextWatcher {
	private float driverSkill = 0;
	private String comments = "";
	private boolean redCard = false, yellowCard = false;
	private boolean deadBot = false;
	private int techCount = 0, foulCount = 0;

	private final void registerClickListener(View v, int id) {
		View vv = v.findViewById(id);
		if (vv != null) {
			vv.setOnClickListener(this);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.review_fouls_skills_fragment,
				container, false);
		View comments = v.findViewById(R.id.reviewComments);
		if (comments != null && comments instanceof EditText) {
			((EditText) comments).addTextChangedListener(this);
		}
		registerClickListener(v, R.id.foulFoulDown);
		registerClickListener(v, R.id.foulFoulUp);
		registerClickListener(v, R.id.foulTechDown);
		registerClickListener(v, R.id.foulTechUp);
		registerClickListener(v, R.id.foulRedCard);
		registerClickListener(v, R.id.foulYellowCard);
		registerClickListener(v, R.id.driverSkill);
		registerClickListener(v, R.id.deadBot);
		return v;
	}

	@Override
	public String getName() {
		return "Fouls and Comments";
	}

	@Override
	public Class<? extends NamedTabFragment> getNext() {
		return AutoLocFragment.class;
	}

	@Override
	public Class<? extends NamedTabFragment> getPrevious() {
		return TeleShootLocFragment.class;
	}

	@Override
	public void afterTextChanged(Editable arg0) {
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		comments = getTextContents(R.id.reviewComments);
	}

	@Override
	public void onClick(View e) {
		switch (e.getId()) {
		case R.id.foulFoulUp:
			foulCount++;
			break;
		case R.id.foulFoulDown:
			if (foulCount > 0) {
				foulCount--;
			}
			break;
		case R.id.foulTechUp:
			techCount++;
			break;
		case R.id.foulTechDown:
			if (techCount > 0) {
				techCount--;
			}
			break;
		case R.id.foulRedCard:
			if (e instanceof CheckBox) {
				redCard = ((CheckBox) e).isChecked();
			}
			break;
		case R.id.foulYellowCard:
			if (e instanceof CheckBox) {
				yellowCard = ((CheckBox) e).isChecked();
			}
			break;
		case R.id.deadBot:
			if (e instanceof CheckBox) {
				deadBot = ((CheckBox) e).isChecked();
			}
			break;
		case R.id.driverSkill:
			if (e instanceof RatingBar) {
				driverSkill = ((RatingBar) e).getRating();
			}
			break;
		}
		updateScores();
	}

	@Override
	public void onResume() {
		super.onResume();
		updateScores();
	}

	private void updateScores() {
		setText(R.id.foulFouls, this.foulCount + " fouls");
		setText(R.id.foulTechs, this.techCount + " technicals");
		setState(R.id.foulRedCard, this.redCard);
		setState(R.id.foulYellowCard, this.yellowCard);
		setState(R.id.deadBot, this.deadBot);
		setTextContents(R.id.reviewComments, comments);

		View v = getView().findViewById(R.id.driverSkill);
		if (v != null && v instanceof RatingBar) {
			((RatingBar) v).setRating(driverSkill);
		}
	}

	@Override
	public void storeInformation(DataCache data) {
		data.putBoolean(DataKeys.MATCH_FOULS_RED_CARD, redCard);
		data.putBoolean(DataKeys.MATCH_FOULS_YELLOW_CARD, yellowCard);
		data.putBoolean(DataKeys.MATCH_DEADBOT, deadBot);
		data.putInteger(DataKeys.MATCH_FOULS_FOULS, foulCount);
		data.putInteger(DataKeys.MATCH_FOULS_TECHNICALS, techCount);
		data.putFloat(DataKeys.MATCH_REVIEW_DRIVER_SKILL, driverSkill);
		data.putString(DataKeys.MATCH_REVIEW_COMMENTS, comments);
	}

	@Override
	public void loadInformation(DataCache data) {
		redCard = data.getBoolean(DataKeys.MATCH_FOULS_RED_CARD, false);
		yellowCard = data.getBoolean(DataKeys.MATCH_FOULS_YELLOW_CARD, false);
		deadBot = data.getBoolean(DataKeys.MATCH_DEADBOT, false);
		foulCount = data.getInteger(DataKeys.MATCH_FOULS_FOULS, 0);
		techCount = data.getInteger(DataKeys.MATCH_FOULS_TECHNICALS, 0);
		driverSkill = data.getFloat(DataKeys.MATCH_REVIEW_DRIVER_SKILL, 0);
		comments = data.getString(DataKeys.MATCH_REVIEW_COMMENTS, "");
	}
}
