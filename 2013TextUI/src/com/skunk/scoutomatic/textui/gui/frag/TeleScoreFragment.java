package com.skunk.scoutomatic.textui.gui.frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.skunk.scoutomatic.textui.DataCache;
import com.skunk.scoutomatic.textui.DataKeys;
import com.skunk.scoutomatic.textui.R;

/**
 * Created on: Sep 20, 2013
 * 
 * @author Westin Miller
 */
public class TeleScoreFragment extends NamedTabFragment implements
		OnClickListener {
	private int score1Discs = 0;
	private int score2Discs = 0;
	private int score3Discs = 0;
	private int scorePyrDiscs = 0;
	private int collectDiscs = 0;
	private int scoreMissed = 0;

	private final void registerViewWithClickListener(View v, int id) {
		View found = v.findViewById(id);
		if (found != null) {
			found.setOnClickListener(this);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		updateScores();
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
		return v;
	}

	@Override
	public String getName() {
		return "Teleop Score";
	}

	@Override
	public Class<? extends NamedTabFragment> getNext() {
		return TeleClimbFragment.class;
	}

	@Override
	public Class<? extends NamedTabFragment> getPrevious() {
		return AutoScoreFragment.class;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.teleCollectUp:
			collectDiscs++;
			break;
		case R.id.teleCollectDown:
			if (collectDiscs > 0) {
				collectDiscs--;
			}
			break;
		case R.id.teleScore1Up:
			score1Discs++;
			break;
		case R.id.teleScore1Down:
			if (score1Discs > 0) {
				score1Discs--;
			}
			break;
		case R.id.teleScore2Up:
			score2Discs++;
			break;
		case R.id.teleScore2Down:
			if (score2Discs > 0) {
				score2Discs--;
			}
			break;
		case R.id.teleScore3Up:
			score3Discs++;
			break;
		case R.id.teleScore3Down:
			if (score3Discs > 0) {
				score3Discs--;
			}
			break;
		case R.id.teleScorePyrUp:
			scorePyrDiscs++;
			break;
		case R.id.teleScorePyrDown:
			if (scorePyrDiscs > 0) {
				scorePyrDiscs--;
			}
			break;
		case R.id.teleScoreMissUp:
			scoreMissed++;
			break;
		case R.id.teleScoreMissDown:
			if (scoreMissed > 0) {
				scoreMissed--;
			}
			break;
		}
		updateScores();
	}

	private void updateScores() {
		setText(R.id.teleCollect, this.collectDiscs + " discs");

		setText(R.id.teleScore1, this.score1Discs + " discs");
		setText(R.id.teleScore2, this.score2Discs + " discs");
		setText(R.id.teleScore3, this.score3Discs + " discs");
		setText(R.id.teleScorePyr, this.scorePyrDiscs + " discs");

		setText(R.id.teleScoreMiss, this.scoreMissed + " discs");

		setText(R.id.teleTotalScore, ((this.score1Discs * 2)
				+ (this.score2Discs * 4) + (this.score3Discs * 6))
				+ " pts");

		if (this.score1Discs + this.score2Discs + this.score3Discs
				+ this.scorePyrDiscs + this.scoreMissed - 2 > this.collectDiscs) {
			setText(R.id.teleScoreWarning,
					"More discs were shot than obtained! ("
							+ (this.score1Discs + this.score2Discs
									+ this.score3Discs + this.scorePyrDiscs + this.scoreMissed)
							+ " > " + (2 + this.collectDiscs) + ")");
		} else {
			setText(R.id.teleScoreWarning, "");
		}
	}

	@Override
	public void storeInformation(DataCache data) {
		data.putInteger(DataKeys.MATCH_TELE_COLLECT, collectDiscs);
		data.putInteger(DataKeys.MATCH_TELE_SCORE_MISS, scoreMissed);
		data.putInteger(DataKeys.MATCH_TELE_SCORE_1, score1Discs);
		data.putInteger(DataKeys.MATCH_TELE_SCORE_2, score2Discs);
		data.putInteger(DataKeys.MATCH_TELE_SCORE_3, score3Discs);
		data.putInteger(DataKeys.MATCH_TELE_SCORE_PYRAMID, scorePyrDiscs);
	}

	@Override
	public void loadInformation(DataCache data) {
		collectDiscs = data.getInteger(DataKeys.MATCH_TELE_COLLECT, 0);
		scoreMissed = data.getInteger(DataKeys.MATCH_TELE_SCORE_MISS, 0);
		score1Discs = data.getInteger(DataKeys.MATCH_TELE_SCORE_1, 0);
		score2Discs = data.getInteger(DataKeys.MATCH_TELE_SCORE_2, 0);
		score3Discs = data.getInteger(DataKeys.MATCH_TELE_SCORE_3, 0);
		scorePyrDiscs = data.getInteger(DataKeys.MATCH_TELE_SCORE_PYRAMID, 0);
	}
}
