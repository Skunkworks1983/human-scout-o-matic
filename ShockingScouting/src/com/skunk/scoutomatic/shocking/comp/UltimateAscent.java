package com.skunk.scoutomatic.shocking.comp;

import com.skunk.scoutomatic.shocking.FieldActivity;
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
		ParentButton scoreParent = new ParentButton("Score", score1, score2,
				score3);

		super.registerButtonTrigger(ButtonManager.HELD_DOWN_EVENT, scoreParent);
	}
}
