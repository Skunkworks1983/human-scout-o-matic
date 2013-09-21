package com.skunk.scoutomatic.textui.gui.frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skunk.scoutomatic.textui.R;

/**
 * Created on: Sep 20, 2013
 * 
 * @author Westin Miller
 */
public class AutoScoreFragment extends NamedTabFragment implements
		OnClickListener {
	private int score2Discs = 0;
	private int score4Discs = 0;
	private int score6Discs = 0;
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
		return v;
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
		switch (v.getId()) {
		case R.id.autoCollectUp:
			collectDiscs++;
			break;
		case R.id.autoCollectDown:
			if (collectDiscs > 0) {
				collectDiscs--;
			}
			break;
		case R.id.autoScore2Up:
			score2Discs++;
			break;
		case R.id.autoScore2Down:
			if (score2Discs > 0) {
				score2Discs--;
			}
			break;
		case R.id.autoScore4Up:
			score4Discs++;
			break;
		case R.id.autoScore4Down:
			if (score4Discs > 0) {
				score4Discs--;
			}
			break;
		case R.id.autoScore6Up:
			score6Discs++;
			break;
		case R.id.autoScore6Down:
			if (score6Discs > 0) {
				score6Discs--;
			}
			break;
		case R.id.autoScoreMissUp:
			scoreMissed++;
			break;
		case R.id.autoScoreMissDown:
			if (scoreMissed > 0) {
				scoreMissed--;
			}
			break;
		}
		updateScores();
	}

	private final void setText(int id, String val) {
		View v = getView().findViewById(id);
		if (v != null && v instanceof TextView) {
			((TextView) v).setText(val);
		}
	}

	private void updateScores() {
		setText(R.id.autoCollect, this.collectDiscs + " discs");

		setText(R.id.autoScore2, this.score2Discs + " discs");
		setText(R.id.autoScore4, this.score4Discs + " discs");
		setText(R.id.autoScore6, this.score6Discs + " discs");

		setText(R.id.autoScoreMiss, this.scoreMissed + " discs");

		setText(R.id.autoTotalScore, ((this.score2Discs * 2)
				+ (this.score4Discs * 4) + (this.score6Discs * 6))
				+ " pts");

		if (this.score2Discs + this.score4Discs + this.score6Discs
				+ this.scoreMissed - 2 > this.collectDiscs) {
			setText(R.id.autoScoreWarning,
					"More discs were shot than obtained! ("
							+ (this.score2Discs + this.score4Discs
									+ this.score6Discs + this.scoreMissed)
							+ " > " + (2 + this.collectDiscs) + ")");
		} else {
			setText(R.id.autoScoreWarning, "");
		}
	}
}
