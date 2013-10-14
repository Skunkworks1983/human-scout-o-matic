package com.skunk.scoutomatic.textui.gui.frag;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;

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
public class TeleScoreFragment extends NamedTabFragmentImpl implements
		OnClickListener, TextWatcher {
	private static final long TELE_END = 105000;

	private static final int MAX_COLLECTED_DISCS = 200;

	private static final int MAX_HOLDING_DISCS = 4;

	private int score1Discs = 0;
	private int score2Discs = 0;
	private int score3Discs = 0;
	private int scorePyrDiscs = 0;
	private int collectDiscs = 0;
	private int scoreMissed = 0;
	private int startingWithDiscs = 2;

	// Actions
	private long matchBegin = -1;
	private List<Action> actionDB = new ArrayList<Action>();

	private final void registerViewWithClickListener(View v, int id) {
		View found = v.findViewById(id);
		if (found != null) {
			found.setOnClickListener(this);
		}
	}

	public int getColor() {
		return 0x440000FF;
	}
	
	@Override
	public void onResume() {
		super.onResume();

		View v = getView().findViewById(R.id.teleCurrentTime);
		if (v != null && v instanceof Chronometer) {
			if (matchBegin > 0) {
				((Chronometer) v).setBase(matchBegin);
				((Chronometer) v).start();
			} else {
				((Chronometer) v).setText("Waiting to start...");
			}
		}

		updateContents();
	}

	@Override
	public void onPause() {
		super.onResume();

		View v = getView().findViewById(R.id.teleCurrentTime);
		if (v != null && v instanceof Chronometer) {
			((Chronometer) v).stop();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.tele_score_fragment, container,
				false);
		registerViewWithClickListener(v, R.id.teleCollectUp);
		registerViewWithClickListener(v, R.id.teleCollectDown);
		registerViewWithClickListener(v, R.id.teleScore1Up);
		registerViewWithClickListener(v, R.id.teleScore1Down);
		registerViewWithClickListener(v, R.id.teleScore2Up);
		registerViewWithClickListener(v, R.id.teleScore2Down);
		registerViewWithClickListener(v, R.id.teleScore3Up);
		registerViewWithClickListener(v, R.id.teleScore3Down);
		registerViewWithClickListener(v, R.id.teleScorePyrUp);
		registerViewWithClickListener(v, R.id.teleScorePyrDown);
		registerViewWithClickListener(v, R.id.teleScoreMissUp);
		registerViewWithClickListener(v, R.id.teleScoreMissDown);

		registerViewWithClickListener(v, R.id.teleCurrentTime);
		View vv = v.findViewById(R.id.teleCurrentTime);
		if (vv != null && vv instanceof TextView) {
			((TextView) vv).addTextChangedListener(this);
		}
		return v;
	}

	@Override
	public String getName() {
		return "Teleop Score";
	}

	@Override
	public Class<? extends NamedTabFragmentImpl> getNext() {
		return TeleClimbFragment.class;
	}

	@Override
	public Class<? extends NamedTabFragmentImpl> getPrevious() {
		return AutoScoreFragment.class;
	}

	@Override
	public void onClick(View v) {
		if (matchBegin == -1) {
			matchBegin = SystemClock.elapsedRealtime();
			View vv = getView().findViewById(R.id.teleCurrentTime);
			if (vv != null && vv instanceof Chronometer) {
				((Chronometer) vv).setBase(matchBegin);
				((Chronometer) vv).start();
			}
		}
		switch (v.getId()) {
		case R.id.teleCollectUp:
			collectDiscs++;
			actionDB.add(new Action(ActionType.TELE_COLLECT,
					ActionResults.COLLECT_1, SystemClock.elapsedRealtime()
							- matchBegin));
			break;
		case R.id.teleCollectDown:
			if (collectDiscs > 0) {
				collectDiscs--;
				ActionCacheUtil.dropLast(actionDB, ActionType.AUTO_COLLECT,
						ActionResults.COLLECT_1, 1);
			}
			break;
		case R.id.teleScore1Up:
			score1Discs++;
			actionDB.add(new Action(ActionType.TELE_SHOOT,
					ActionResults.SHOOT_SCORE1, SystemClock.elapsedRealtime()
							- matchBegin));
			break;
		case R.id.teleScore1Down:
			if (score1Discs > 0) {
				score1Discs--;
				ActionCacheUtil.dropLast(actionDB, ActionType.TELE_SHOOT,
						ActionResults.SHOOT_SCORE1, 1);
			}
			break;
		case R.id.teleScore2Up:
			score2Discs++;
			actionDB.add(new Action(ActionType.TELE_SHOOT,
					ActionResults.SHOOT_SCORE2, SystemClock.elapsedRealtime()
							- matchBegin));
			break;
		case R.id.teleScore2Down:
			if (score2Discs > 0) {
				score2Discs--;
				ActionCacheUtil.dropLast(actionDB, ActionType.TELE_SHOOT,
						ActionResults.SHOOT_SCORE2, 1);
			}
			break;
		case R.id.teleScore3Up:
			score3Discs++;
			actionDB.add(new Action(ActionType.TELE_SHOOT,
					ActionResults.SHOOT_SCORE3, SystemClock.elapsedRealtime()
							- matchBegin));
			break;
		case R.id.teleScore3Down:
			if (score3Discs > 0) {
				score3Discs--;
				ActionCacheUtil.dropLast(actionDB, ActionType.TELE_SHOOT,
						ActionResults.SHOOT_SCORE3, 1);
			}
			break;
		case R.id.teleScorePyrUp:
			scorePyrDiscs++;
			actionDB.add(new Action(ActionType.TELE_SHOOT,
					ActionResults.SHOOT_SCORE5, SystemClock.elapsedRealtime()
							- matchBegin));
			break;
		case R.id.teleScorePyrDown:
			if (scorePyrDiscs > 0) {
				scorePyrDiscs--;
				ActionCacheUtil.dropLast(actionDB, ActionType.TELE_SHOOT,
						ActionResults.SHOOT_SCORE5, 1);
			}
			break;
		case R.id.teleScoreMissUp:
			scoreMissed++;
			actionDB.add(new Action(ActionType.TELE_SHOOT,
					ActionResults.SHOOT_MISS, SystemClock.elapsedRealtime()
							- matchBegin));
			break;
		case R.id.teleScoreMissDown:
			if (scoreMissed > 0) {
				scoreMissed--;
				ActionCacheUtil.dropLast(actionDB, ActionType.TELE_SHOOT,
						ActionResults.SHOOT_MISS, 1);
			}
			break;
		}
		updateContents();
	}

	protected void updateContents() {
		setText(R.id.teleCollect, this.collectDiscs + " discs");

		setText(R.id.teleScore1, this.score1Discs + " discs");
		setText(R.id.teleScore2, this.score2Discs + " discs");
		setText(R.id.teleScore3, this.score3Discs + " discs");
		setText(R.id.teleScorePyr, this.scorePyrDiscs + " discs");

		setText(R.id.teleScoreMiss, this.scoreMissed + " discs");

		setText(R.id.teleTotalScore, ((this.score1Discs * 2)
				+ (this.score2Discs * 4) + (this.score3Discs * 6))
				+ " pts");

		StringBuilder warnings = new StringBuilder();
		
		int shotDiscs = this.score1Discs + this.score2Discs + this.score3Discs
				+ this.scorePyrDiscs + this.scoreMissed;
		int touchedDiscs = startingWithDiscs + this.collectDiscs;
		if (shotDiscs > touchedDiscs) {
			warnings.append("More discs were shot than obtained! (" + shotDiscs
					+ " > " + touchedDiscs + ")");
			warnings.append("\n");
		}
		if ((touchedDiscs - shotDiscs) > MAX_HOLDING_DISCS) {
			warnings.append("The robot appears to be holding "
					+ (touchedDiscs - shotDiscs) + " discs.\n");
		}
		if (this.collectDiscs > MAX_COLLECTED_DISCS) {
			warnings.append("More discs were collected than reasonable.\n");
		}
		if (matchBegin > 0) {
			long elapsed = SystemClock.elapsedRealtime() - matchBegin;
			if (elapsed > TELE_END) {
				warnings.append("This period has likely ended!");
				warnings.append("\n");
			}
		}
		setText(R.id.teleScoreWarning, warnings.toString());
	}

	@Override
	public void storeInformation(DataCache data) {
		data.putInteger(DataKeys.MATCH_TELE_COLLECT, collectDiscs);
		data.putInteger(DataKeys.MATCH_TELE_SCORE_MISS, scoreMissed);
		data.putInteger(DataKeys.MATCH_TELE_SCORE_1, score1Discs);
		data.putInteger(DataKeys.MATCH_TELE_SCORE_2, score2Discs);
		data.putInteger(DataKeys.MATCH_TELE_SCORE_3, score3Discs);
		data.putInteger(DataKeys.MATCH_TELE_SCORE_PYRAMID, scorePyrDiscs);

		// Actions
		data.putList(DataKeys.MATCH_ACTIONS_KEY, actionDB);
		data.putLong(DataKeys.MATCH_START_KEY, matchBegin);
	}

	@Override
	public void loadInformation(DataCache data) {
		collectDiscs = data.getInteger(DataKeys.MATCH_TELE_COLLECT, 0);
		scoreMissed = data.getInteger(DataKeys.MATCH_TELE_SCORE_MISS, 0);
		score1Discs = data.getInteger(DataKeys.MATCH_TELE_SCORE_1, 0);
		score2Discs = data.getInteger(DataKeys.MATCH_TELE_SCORE_2, 0);
		score3Discs = data.getInteger(DataKeys.MATCH_TELE_SCORE_3, 0);
		scorePyrDiscs = data.getInteger(DataKeys.MATCH_TELE_SCORE_PYRAMID, 0);
		startingWithDiscs = data.getInteger(DataKeys.MATCH_AUTO_STARTING_DISCS,
				2)
				+ data.getInteger(DataKeys.MATCH_AUTO_COLLECT, 0)
				- data.getInteger(DataKeys.MATCH_AUTO_SCORE_2, 0)
				- data.getInteger(DataKeys.MATCH_AUTO_SCORE_4, 0)
				- data.getInteger(DataKeys.MATCH_AUTO_SCORE_6, 0)
				- data.getInteger(DataKeys.MATCH_AUTO_SCORE_MISS, 0);

		// Actions
		matchBegin = data.getLong(DataKeys.MATCH_START_KEY, -1);
		actionDB = data.getList(DataKeys.MATCH_ACTIONS_KEY, Action.class);
	}

	@Override
	public boolean needsKeyboard() {
		return false;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (matchBegin > 0) {
			long elapsed = SystemClock.elapsedRealtime() - matchBegin;
			if (elapsed > TELE_END) {
				updateContents();
			}
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
	}
}
