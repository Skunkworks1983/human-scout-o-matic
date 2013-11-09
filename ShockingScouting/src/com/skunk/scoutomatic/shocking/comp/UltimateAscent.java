package com.skunk.scoutomatic.shocking.comp;

import com.skunk.scoutomatic.shocking.FieldActivity;
import com.skunk.scoutomatic.shocking.control.ActActionButton;
import com.skunk.scoutomatic.shocking.control.ButtonManager;
import com.skunk.scoutomatic.shocking.control.DBActionButton;
import com.skunk.scoutomatic.shocking.control.ParentButton;

public class UltimateAscent extends YearlyGame {
	public UltimateAscent(FieldActivity act) {
		super(act);
	}

	@Override
	public void init() {
		DBActionButton score1 = new DBActionButton("Score 1", "SCORE_TELEOP",
				"1");
		DBActionButton score2 = new DBActionButton("Score 2", "SCORE_TELEOP",
				"2");
		DBActionButton score3 = new DBActionButton("Score 3", "SCORE_TELEOP",
				"3");
		DBActionButton score5 = new DBActionButton("Score 5", "SCORE_TELEOP",
				"5");
		DBActionButton scoreMiss = new DBActionButton("Miss", "SCORE_TELEOP",
				"miss");
		ParentButton scoreParent = new ParentButton("Score",
				ParentButton.CACHING, score1, score2, score3, score5, scoreMiss);

		super.registerButtonTrigger(ButtonManager.HELD_DOWN_1_EVENT,
				scoreParent);
		super.registerButtonTrigger(ButtonManager.HELD_DOWN_MULTI_EVENT,
				createLoadingComments());
	}

	private static ParentButton createLoadingComments() {
		ActActionButton badLoading = new ActActionButton("Bad Loading",
				"comment-load", "bad");
		ActActionButton slowLoading = new ActActionButton("Slow Loading",
				"comment-load", "slow");
		ActActionButton fastLoading = new ActActionButton("Fast Loading",
				"comment-load", "fast");
		return new ParentButton("Loading", ParentButton.SINGLE_SHOT,
				badLoading, slowLoading, fastLoading);
	}
}
