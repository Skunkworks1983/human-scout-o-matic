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
				new ParentButton("Comment", 0, createLoadingComments(),
						createDefensiveComments()));
	}

	private static ParentButton createLoadingComments() {
		ActActionButton badLoading = new ActActionButton("Bad Load",
				"comment-load", "bad");
		ActActionButton slowLoading = new ActActionButton("Slow Load",
				"comment-load", "slow");
		ActActionButton fastLoading = new ActActionButton("Fast Load",
				"comment-load", "fast");
		return new ParentButton("Loading", ParentButton.SINGLE_SHOT,
				badLoading, slowLoading, fastLoading) {
			public boolean isVisible(FieldActivity act) {
				return act.getRobotX() >= 0.7 || act.getRobotX() <= 0.3;
			}
		};
	}

	private static ParentButton createDefensiveComments() {
		ActActionButton goodDef = new ActActionButton("Good Def",
				"comment-defense", "good");
		ActActionButton badDef = new ActActionButton("Bad Def",
				"comment-defense", "bad");
		return new ParentButton("Defense", ParentButton.SINGLE_SHOT, goodDef,
				badDef);
	}
}
