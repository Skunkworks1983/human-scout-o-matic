package com.skunk.scoutomatic.textui.gui.frag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pi.scoutomatic.lib.data.DataCache;
import com.skunk.scoutomatic.textui.Action;
import com.skunk.scoutomatic.textui.ActionCacheUtil;
import com.skunk.scoutomatic.textui.ActionResults;
import com.skunk.scoutomatic.textui.ActionType;
import com.skunk.scoutomatic.textui.DataKeys;
import com.skunk.scoutomatic.textui.R;
import com.skunk.scoutomatic.textui.gui.frag.dummy.SubmitFragment;

/**
 * Created on: Sep 21, 2013
 * 
 * @author "Westin Miller"
 * 
 */
public class ReviewAllFragment extends NamedTabFragmentImpl {
	private static final int MAXIMUM_TELE_SCORE = 1000;
	private static final int MAXIMUM_AUTO_SCORE = 15;
	private static final int MAXIMUM_FOULS = 9;
	private DataCache loadOnCreate;

	// Actions
	private List<Action> actionDB = new ArrayList<Action>();

	@Override
	public String getName() {
		return "Review";
	}

	public int getColor() {
		return 0x99556B2F;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.review_all_fragment, container,
				false);
		setNumberRange(v, R.id.reviewAutoCollect, 0, MAXIMUM_AUTO_SCORE);
		setNumberRange(v, R.id.reviewAutoScore2, 0, MAXIMUM_AUTO_SCORE);
		setNumberRange(v, R.id.reviewAutoScore4, 0, MAXIMUM_AUTO_SCORE);
		setNumberRange(v, R.id.reviewAutoScore6, 0, MAXIMUM_AUTO_SCORE);
		setNumberRange(v, R.id.reviewAutoMiss, 0, MAXIMUM_AUTO_SCORE);

		setNumberRange(v, R.id.reviewTeleCollect, 0, MAXIMUM_TELE_SCORE);
		setNumberRange(v, R.id.reviewTeleScoreMiss, 0, MAXIMUM_TELE_SCORE);
		setNumberRange(v, R.id.reviewTeleScore1, 0, MAXIMUM_TELE_SCORE);
		setNumberRange(v, R.id.reviewTeleScore2, 0, MAXIMUM_TELE_SCORE);
		setNumberRange(v, R.id.reviewTeleScore3, 0, MAXIMUM_TELE_SCORE);
		setNumberRange(v, R.id.reviewTeleScorePyr, 0, MAXIMUM_TELE_SCORE);

		setNumberRange(v, R.id.reviewClimbLevel, 0, 3);

		setNumberRange(v, R.id.reviewFoulFoul, 0, MAXIMUM_FOULS);
		setNumberRange(v, R.id.reviewFoulTechnical, 0, MAXIMUM_FOULS);
		return v;
	}

	public void onResume() {
		super.onResume();
		if (loadOnCreate != null) {
			loadInformation(loadOnCreate);
		}
	}

	@Override
	public Class<? extends NamedTabFragmentImpl> getNext() {
		return SubmitFragment.class;
	}

	@Override
	public Class<? extends NamedTabFragmentImpl> getPrevious() {
		return ReviewFoulsSkillsFragment.class;
	}

	@Override
	public void storeInformation(DataCache data) {
		data.putInteger(DataKeys.MATCH_AUTO_COLLECT,
				getNumberValue(R.id.reviewAutoCollect));
		data.putInteger(DataKeys.MATCH_AUTO_SCORE_2,
				getNumberValue(R.id.reviewAutoScore2));
		data.putInteger(DataKeys.MATCH_AUTO_SCORE_4,
				getNumberValue(R.id.reviewAutoScore4));
		data.putInteger(DataKeys.MATCH_AUTO_SCORE_6,
				getNumberValue(R.id.reviewAutoScore6));
		data.putInteger(DataKeys.MATCH_AUTO_SCORE_MISS,
				getNumberValue(R.id.reviewAutoMiss));

		data.putInteger(DataKeys.MATCH_TELE_COLLECT,
				getNumberValue(R.id.reviewTeleCollect));
		data.putInteger(DataKeys.MATCH_TELE_SCORE_MISS,
				getNumberValue(R.id.reviewTeleScoreMiss));
		data.putInteger(DataKeys.MATCH_TELE_SCORE_1,
				getNumberValue(R.id.reviewTeleScore1));
		data.putInteger(DataKeys.MATCH_TELE_SCORE_2,
				getNumberValue(R.id.reviewTeleScore2));
		data.putInteger(DataKeys.MATCH_TELE_SCORE_3,
				getNumberValue(R.id.reviewTeleScore3));
		data.putInteger(DataKeys.MATCH_TELE_SCORE_PYRAMID,
				getNumberValue(R.id.reviewTeleScorePyr));

		data.putInteger(DataKeys.MATCH_TELE_CLIMB_LEVEL,
				getNumberValue(R.id.reviewClimbLevel));
		data.putBoolean(DataKeys.MATCH_TELE_CLIMB_ATTEMPTED,
				getState(R.id.reviewClimbAttempt));

		data.putInteger(DataKeys.MATCH_FOULS_FOULS,
				getNumberValue(R.id.reviewFoulFoul));
		data.putInteger(DataKeys.MATCH_FOULS_TECHNICALS,
				getNumberValue(R.id.reviewFoulTechnical));
		data.putBoolean(DataKeys.MATCH_FOULS_RED_CARD,
				getState(R.id.reviewFoulRedCard));
		data.putBoolean(DataKeys.MATCH_FOULS_YELLOW_CARD,
				getState(R.id.reviewFoulYellowCard));

		data.putBoolean(DataKeys.MATCH_DEADBOT, getState(R.id.reviewDeadBot));
		data.putBoolean(DataKeys.MATCH_NO_SHOW, getState(R.id.reviewNoShow));

		data.putString(DataKeys.MATCH_SCOUT,
				getTextContents(R.id.reviewScoutName).toString());
		data.putString(DataKeys.MATCH_COMPETITION,
				getTextContents(R.id.reviewCompName).toString());
		String teamValue = getTextContents(R.id.reviewTeamID).toString();
		if (teamValue.length() == 0) {
			teamValue = "0";
		}
		String matchValue = getTextContents(R.id.reviewMatchID).toString();
		if (matchValue.length() == 0) {
			matchValue = "0";
		}
		data.putInteger(DataKeys.MATCH_TEAM, Integer.valueOf(teamValue));
		data.putInteger(DataKeys.MATCH_NUMBER, Integer.valueOf(matchValue));

		// Actions
		// Create actionDB
		// Autonomous
		ActionCacheUtil.forceCount(actionDB, ActionType.AUTO_COLLECT,
				ActionResults.COLLECT_1,
				data.getInteger(DataKeys.MATCH_AUTO_COLLECT, 0));
		ActionCacheUtil.forceCount(actionDB, ActionType.AUTO_SHOOT,
				ActionResults.SHOOT_SCORE2,
				data.getInteger(DataKeys.MATCH_AUTO_SCORE_2, 0));
		ActionCacheUtil.forceCount(actionDB, ActionType.AUTO_SHOOT,
				ActionResults.SHOOT_SCORE4,
				data.getInteger(DataKeys.MATCH_AUTO_SCORE_4, 0));
		ActionCacheUtil.forceCount(actionDB, ActionType.AUTO_SHOOT,
				ActionResults.SHOOT_SCORE6,
				data.getInteger(DataKeys.MATCH_AUTO_SCORE_6, 0));
		ActionCacheUtil.forceCount(actionDB, ActionType.AUTO_SHOOT,
				ActionResults.SHOOT_MISS,
				data.getInteger(DataKeys.MATCH_AUTO_SCORE_MISS, 0));
		ActionCacheUtil.setPositions(actionDB, ActionType.AUTO_SHOOT, null,
				data.getFloat(DataKeys.MATCH_AUTO_LOC_X, 0),
				data.getFloat(DataKeys.MATCH_AUTO_LOC_Y, 0));

		// Teleop Shooting
		ActionCacheUtil.forceCount(actionDB, ActionType.TELE_COLLECT,
				ActionResults.COLLECT_1,
				data.getInteger(DataKeys.MATCH_TELE_COLLECT, 0));
		ActionCacheUtil.forceCount(actionDB, ActionType.TELE_SHOOT,
				ActionResults.SHOOT_SCORE1,
				data.getInteger(DataKeys.MATCH_TELE_SCORE_1, 0));
		ActionCacheUtil.forceCount(actionDB, ActionType.TELE_SHOOT,
				ActionResults.SHOOT_SCORE2,
				data.getInteger(DataKeys.MATCH_TELE_SCORE_2, 0));
		ActionCacheUtil.forceCount(actionDB, ActionType.TELE_SHOOT,
				ActionResults.SHOOT_SCORE3,
				data.getInteger(DataKeys.MATCH_TELE_SCORE_3, 0));
		ActionCacheUtil.forceCount(actionDB, ActionType.TELE_SHOOT,
				ActionResults.SHOOT_SCORE5,
				data.getInteger(DataKeys.MATCH_TELE_SCORE_PYRAMID, 0));
		ActionCacheUtil.forceCount(actionDB, ActionType.TELE_SHOOT,
				ActionResults.SHOOT_MISS,
				data.getInteger(DataKeys.MATCH_TELE_SCORE_MISS, 0));
		ActionCacheUtil.setPositions(actionDB, ActionType.TELE_SHOOT, null,
				data.getFloat(DataKeys.MATCH_TELE_SHOOT_LOC_X, 0),
				data.getFloat(DataKeys.MATCH_TELE_SHOOT_LOC_Y, 0));

		// Tele Climbing
		if (data.getBoolean(DataKeys.MATCH_TELE_CLIMB_ATTEMPTED, false)) {
			Action aa = ActionCacheUtil.getInsuredActionByType(actionDB,
					ActionType.TELE_CLIMB);
			switch (data.getInteger(DataKeys.MATCH_TELE_CLIMB_LEVEL, 0)) {
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

		// Fouls review
		ActionCacheUtil.forceCount(actionDB, ActionType.FOUL,
				ActionResults.FOUL_GENERIC,
				data.getInteger(DataKeys.MATCH_FOULS_FOULS, 0));
		ActionCacheUtil.forceCount(actionDB, ActionType.FOUL,
				ActionResults.FOUL_TECHNICAL,
				data.getInteger(DataKeys.MATCH_FOULS_TECHNICALS, 0));
		ActionCacheUtil.forceCount(actionDB, ActionType.FOUL,
				ActionResults.FOUL_CARD_YELLOW, data.getBoolean(
						DataKeys.MATCH_FOULS_YELLOW_CARD, false) ? 1 : 0);
		ActionCacheUtil.forceCount(actionDB, ActionType.FOUL,
				ActionResults.FOUL_CARD_RED,
				data.getBoolean(DataKeys.MATCH_FOULS_RED_CARD, false) ? 1 : 0);

		data.putList(DataKeys.MATCH_ACTIONS_KEY, actionDB);
	}

	@Override
	public void loadInformation(DataCache data) {
		if (getView() != null) {
			setNumberValue(R.id.reviewAutoCollect,
					data.getInteger(DataKeys.MATCH_AUTO_COLLECT, 0));
			setNumberValue(R.id.reviewAutoScore2,
					data.getInteger(DataKeys.MATCH_AUTO_SCORE_2, 0));
			setNumberValue(R.id.reviewAutoScore4,
					data.getInteger(DataKeys.MATCH_AUTO_SCORE_4, 0));
			setNumberValue(R.id.reviewAutoScore6,
					data.getInteger(DataKeys.MATCH_AUTO_SCORE_6, 0));
			setNumberValue(R.id.reviewAutoMiss,
					data.getInteger(DataKeys.MATCH_AUTO_SCORE_MISS, 0));

			setNumberValue(R.id.reviewTeleCollect,
					data.getInteger(DataKeys.MATCH_TELE_COLLECT, 0));
			setNumberValue(R.id.reviewTeleScoreMiss,
					data.getInteger(DataKeys.MATCH_TELE_SCORE_MISS, 0));
			setNumberValue(R.id.reviewTeleScore1,
					data.getInteger(DataKeys.MATCH_TELE_SCORE_1, 0));
			setNumberValue(R.id.reviewTeleScore2,
					data.getInteger(DataKeys.MATCH_TELE_SCORE_2, 0));
			setNumberValue(R.id.reviewTeleScore3,
					data.getInteger(DataKeys.MATCH_TELE_SCORE_3, 0));
			setNumberValue(R.id.reviewTeleScorePyr,
					data.getInteger(DataKeys.MATCH_TELE_SCORE_PYRAMID, 0));

			setNumberValue(R.id.reviewClimbLevel,
					data.getInteger(DataKeys.MATCH_TELE_CLIMB_LEVEL, 0));
			setState(R.id.reviewClimbAttempt,
					data.getBoolean(DataKeys.MATCH_TELE_CLIMB_ATTEMPTED, false));

			setNumberValue(R.id.reviewFoulFoul,
					data.getInteger(DataKeys.MATCH_FOULS_FOULS, 0));
			setNumberValue(R.id.reviewFoulTechnical,
					data.getInteger(DataKeys.MATCH_FOULS_TECHNICALS, 0));
			setState(R.id.reviewFoulRedCard,
					data.getBoolean(DataKeys.MATCH_FOULS_RED_CARD, false));
			setState(R.id.reviewFoulYellowCard,
					data.getBoolean(DataKeys.MATCH_FOULS_YELLOW_CARD, false));

			setState(R.id.reviewDeadBot,
					data.getBoolean(DataKeys.MATCH_DEADBOT, false));
			setState(R.id.reviewNoShow,
					data.getBoolean(DataKeys.MATCH_NO_SHOW, false));

			setText(R.id.reviewStartingPosition,
					data.getFloat(DataKeys.MATCH_AUTO_LOC_X, 0) + ","
							+ data.getFloat(DataKeys.MATCH_AUTO_LOC_Y, 0));
			setText(R.id.reviewShootingPosition,
					data.getFloat(DataKeys.MATCH_TELE_SHOOT_LOC_X, 0) + ","
							+ data.getFloat(DataKeys.MATCH_TELE_SHOOT_LOC_Y, 0));

			setTextContents(R.id.reviewScoutName,
					data.getString(DataKeys.MATCH_SCOUT, ""));
			setTextContents(R.id.reviewCompName,
					data.getString(DataKeys.MATCH_COMPETITION, ""));
			int teamID = data.getInteger(DataKeys.MATCH_TEAM, 0);
			int matchID = data.getInteger(DataKeys.MATCH_NUMBER, 0);
			setTextContents(R.id.reviewTeamID,
					teamID > 0 ? Integer.toString(teamID) : "");
			setTextContents(R.id.reviewMatchID,
					matchID > 0 ? Integer.toString(matchID) : "");

			// Actions
			actionDB = data.getList(DataKeys.MATCH_ACTIONS_KEY, Action.class);
		} else {
			loadOnCreate = data;
		}
	}

	@Override
	protected void updateContents() {
		if (loadOnCreate != null) {
			loadInformation(loadOnCreate);// TODO This loading directly is janky
		}
	}

	@Override
	public boolean needsKeyboard() {
		return false;
	}
}
