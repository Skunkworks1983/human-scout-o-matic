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

import com.skunk.scoutomatic.textui.Action;
import com.skunk.scoutomatic.textui.ActionCacheUtil;
import com.skunk.scoutomatic.textui.ActionResults;
import com.skunk.scoutomatic.textui.ActionType;
import com.skunk.scoutomatic.textui.DataCache;
import com.skunk.scoutomatic.textui.DataKeys;
import com.skunk.scoutomatic.textui.R;

/**
 * Created on: Sep 20, 2013
 * 
 * @author Westin Miller
 */
public class AutoScoreFragment extends NamedTabFragment implements
		OnClickListener, TextWatcher {
	private static final long AUTO_END = 15000;
	private static final int MAX_COLLECTED_DISCS = 6;
	private static final int MAX_HOLDING_DISCS = 4;

	private int score2Discs = 0;
	private int score4Discs = 0;
	private int score6Discs = 0;
	private int collectDiscs = 0;
	private int scoreMissed = 0;

	private int startingWithDiscs = 2;

	// Actions
	private long matchBegin = -1;
	private List<Action> actionDB = new ArrayList<Action>();

	public int getColor() {
		return 0x44FF0000;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.auto_score_fragment, container,
				false);
		registerViewWithClickListener(v, R.id.autoCollectUp);
		registerViewWithClickListener(v, R.id.autoCollectDown);
		registerViewWithClickListener(v, R.id.autoScore2Up);
		registerViewWithClickListener(v, R.id.autoScore2Down);
		registerViewWithClickListener(v, R.id.autoScore4Up);
		registerViewWithClickListener(v, R.id.autoScore4Down);
		registerViewWithClickListener(v, R.id.autoScore6Up);
		registerViewWithClickListener(v, R.id.autoScore6Down);
		registerViewWithClickListener(v, R.id.autoScoreMissUp);
		registerViewWithClickListener(v, R.id.autoScoreMissDown);

		registerViewWithClickListener(v, R.id.autoCurrentTime);
		View vv = v.findViewById(R.id.autoCurrentTime);
		if (vv != null && vv instanceof TextView) {
			((TextView) vv).addTextChangedListener(this);
		}
		return v;
	}

	private final void registerViewWithClickListener(View v, int id) {
		View found = v.findViewById(id);
		if (found != null) {
			found.setOnClickListener(this);
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		View v = getView().findViewById(R.id.autoCurrentTime);
		if (v != null && v instanceof Chronometer) {
			if (matchBegin >= 0) {
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

		View v = getView().findViewById(R.id.autoCurrentTime);
		if (v != null && v instanceof Chronometer) {
			((Chronometer) v).stop();
		}
	}

	@Override
	public String getName() {
		return "Autonomous Score";
	}

	@Override
	public Class<? extends NamedTabFragment> getNext() {
		return TeleScoreFragment.class;
	}

	@Override
	public Class<? extends NamedTabFragment> getPrevious() {
		return AutoLocFragment.class;
	}

	@Override
	public void onClick(View v) {
		if (matchBegin == -1) {
			matchBegin = SystemClock.elapsedRealtime();
			View vv = getView().findViewById(R.id.autoCurrentTime);
			if (vv != null && vv instanceof Chronometer) {
				((Chronometer) vv).setBase(matchBegin);
				((Chronometer) vv).start();
			}
		}
		switch (v.getId()) {
		case R.id.autoCollectUp:
			collectDiscs++;
			actionDB.add(new Action(ActionType.AUTO_COLLECT,
					ActionResults.COLLECT_1, SystemClock.elapsedRealtime()
							- matchBegin));
			break;
		case R.id.autoCollectDown:
			if (collectDiscs > 0) {
				collectDiscs--;
				ActionCacheUtil.dropLast(actionDB, ActionType.AUTO_COLLECT,
						ActionResults.COLLECT_1, 1);
			}
			break;
		case R.id.autoScore2Up:
			score2Discs++;
			actionDB.add(new Action(ActionType.AUTO_SHOOT,
					ActionResults.SHOOT_SCORE2, SystemClock.elapsedRealtime()
							- matchBegin));
			break;
		case R.id.autoScore2Down:
			if (score2Discs > 0) {
				score2Discs--;
				ActionCacheUtil.dropLast(actionDB, ActionType.AUTO_SHOOT,
						ActionResults.SHOOT_SCORE2, 1);
			}
			break;
		case R.id.autoScore4Up:
			score4Discs++;
			actionDB.add(new Action(ActionType.AUTO_SHOOT,
					ActionResults.SHOOT_SCORE4, SystemClock.elapsedRealtime()
							- matchBegin));
			break;
		case R.id.autoScore4Down:
			if (score4Discs > 0) {
				score4Discs--;
				ActionCacheUtil.dropLast(actionDB, ActionType.AUTO_SHOOT,
						ActionResults.SHOOT_SCORE4, 1);
			}
			break;
		case R.id.autoScore6Up:
			score6Discs++;
			actionDB.add(new Action(ActionType.AUTO_SHOOT,
					ActionResults.SHOOT_SCORE6, SystemClock.elapsedRealtime()
							- matchBegin));
			break;
		case R.id.autoScore6Down:
			if (score6Discs > 0) {
				score6Discs--;
				ActionCacheUtil.dropLast(actionDB, ActionType.AUTO_SHOOT,
						ActionResults.SHOOT_SCORE6, 1);
			}
			break;
		case R.id.autoScoreMissUp:
			scoreMissed++;
			actionDB.add(new Action(ActionType.AUTO_SHOOT,
					ActionResults.SHOOT_MISS, SystemClock.elapsedRealtime()
							- matchBegin));
			break;
		case R.id.autoScoreMissDown:
			if (scoreMissed > 0) {
				scoreMissed--;
				ActionCacheUtil.dropLast(actionDB, ActionType.AUTO_SHOOT,
						ActionResults.SHOOT_MISS, 1);
			}
			break;
		}
		updateContents();
	}

	protected void updateContents() {
		setText(R.id.autoCollect, this.collectDiscs + " discs");

		setText(R.id.autoScore2, this.score2Discs + " discs");
		setText(R.id.autoScore4, this.score4Discs + " discs");
		setText(R.id.autoScore6, this.score6Discs + " discs");

		setText(R.id.autoScoreMiss, this.scoreMissed + " discs");

		setText(R.id.autoTotalScore, ((this.score2Discs * 2)
				+ (this.score4Discs * 4) + (this.score6Discs * 6))
				+ " pts");

		StringBuilder warnings = new StringBuilder();

		int shotDiscs = this.score2Discs + this.score4Discs + this.score6Discs
				+ this.scoreMissed;
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
			if (elapsed > AUTO_END) {
				warnings.append("This period has likely ended!");
				warnings.append("\n");
			}
		}
		setText(R.id.autoScoreWarning, warnings.toString());
	}

	@Override
	public void storeInformation(DataCache data) {
		data.putInteger(DataKeys.MATCH_AUTO_COLLECT, collectDiscs);
		data.putInteger(DataKeys.MATCH_AUTO_SCORE_MISS, scoreMissed);
		data.putInteger(DataKeys.MATCH_AUTO_SCORE_2, score2Discs);
		data.putInteger(DataKeys.MATCH_AUTO_SCORE_4, score4Discs);
		data.putInteger(DataKeys.MATCH_AUTO_SCORE_6, score6Discs);

		// Actions
		data.putList(DataKeys.MATCH_ACTIONS_KEY, actionDB);
		data.putLong(DataKeys.MATCH_START_KEY, matchBegin);
	}

	@Override
	public void loadInformation(DataCache data) {
		collectDiscs = data.getInteger(DataKeys.MATCH_AUTO_COLLECT, 0);
		scoreMissed = data.getInteger(DataKeys.MATCH_AUTO_SCORE_MISS, 0);
		score2Discs = data.getInteger(DataKeys.MATCH_AUTO_SCORE_2, 0);
		score4Discs = data.getInteger(DataKeys.MATCH_AUTO_SCORE_4, 0);
		score6Discs = data.getInteger(DataKeys.MATCH_AUTO_SCORE_6, 0);
		startingWithDiscs = data.getInteger(DataKeys.MATCH_AUTO_STARTING_DISCS,
				2);

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
			if (elapsed > AUTO_END) {
				updateContents();
			}
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
	}
}
