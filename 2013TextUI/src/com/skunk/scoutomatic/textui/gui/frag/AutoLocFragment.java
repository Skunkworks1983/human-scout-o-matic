package com.skunk.scoutomatic.textui.gui.frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pi.scoutomatic.lib.data.DataCache;
import com.skunk.scoutomatic.textui.DataKeys;
import com.skunk.scoutomatic.textui.R;

/**
 * Created on: Sep 20, 2013
 * 
 * @author Westin Miller
 */
public class AutoLocFragment extends NamedTabFragmentImpl implements
		OnTouchListener, OnClickListener {
	public static final float MIN_TRIPLE_Y = 62.5f;
	public static final float MAX_TRIPLE_Y = 100;

	private static final float minX = 25;
	private static final float minY = 30;
	private static final float maxX = 75;
	private static final float maxY = 75;

	private float xPos = 0, yPos = 0;
	private boolean noShow = false;

	private int loadingDiscs = 2;

	public int getColor() {
		return 0x33FFA500;
	}

	private final void registerClickListener(View v, int id) {
		View vv = v.findViewById(id);
		if (vv != null) {
			vv.setOnClickListener(this);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.auto_loc_fragment, container, false);
		View autoSheet = v.findViewById(R.id.autoScoutSheet);
		if (autoSheet != null) {
			autoSheet.setOnTouchListener(this);
		}
		registerClickListener(v, R.id.matchNoShow);
		registerClickListener(v, R.id.autoLoad2);
		registerClickListener(v, R.id.autoLoad3);
		return v;
	}

	@Override
	public String getName() {
		return "Autonomous Location";
	}

	@Override
	public Class<? extends NamedTabFragmentImpl> getNext() {
		return AutoScoreFragment.class;
	}

	@Override
	public Class<? extends NamedTabFragmentImpl> getPrevious() {
		return WelcomeFragment.class;
	}

	@Override
	public void onResume() {
		super.onResume();
		updateContents();
		postUpdate();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			switch (v.getId()) {
			case R.id.autoScoutSheet:
				float txPos = (event.getX() / v.getWidth()) * 100;
				float tyPos = (event.getY() / v.getHeight()) * 100;
				if (txPos >= minX && tyPos >= minY && txPos < maxX
						&& tyPos < maxY) {
					xPos = txPos;
					yPos = tyPos;
					loadingDiscs = yPos >= MIN_TRIPLE_Y && yPos < MAX_TRIPLE_Y ? 3
							: 2;
					updateContents();
				}
				break;
			}
			return true;
		}
		return true;
	}

	protected void updateContents() {
		setState(R.id.matchNoShow, noShow);
		setState(R.id.autoLoad2, loadingDiscs == 2);
		setState(R.id.autoLoad3, loadingDiscs == 3);

		if (xPos == 0.0f && yPos == 0.0f) {
			return;
		}
		View v = getView().findViewById(R.id.autoScoutSheet);
		View vv = getView().findViewById(R.id.autoFirstRobot);
		View textView = getView().findViewById(R.id.autoRobotLocation);
		if (textView != null && textView instanceof TextView) {
			((TextView) textView).setText(xPos + "," + yPos + "\n" + "Loading "
					+ loadingDiscs + " discs");
		}
		if (v != null && vv != null) {
			vv.setVisibility(View.VISIBLE);
			((RelativeLayout.LayoutParams) vv.getLayoutParams()).setMargins(
					(int) (xPos / 100.0f * v.getWidth()) - (vv.getWidth() / 2),
					(int) (yPos / 100.0f * v.getHeight())
							- (vv.getHeight() / 2), 0, 0);
			vv.requestLayout();
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.matchNoShow && v instanceof CheckBox) {
			noShow = ((CheckBox) v).isChecked();
		} else if (v.getId() == R.id.autoLoad2 && v instanceof RadioButton) {
			loadingDiscs = ((RadioButton) v).isChecked() ? 2 : 3;
		} else if (v.getId() == R.id.autoLoad3 && v instanceof RadioButton) {
			loadingDiscs = ((RadioButton) v).isChecked() ? 3 : 2;
		}
		updateContents();
	}

	@Override
	public void storeInformation(DataCache data) {
		data.putBoolean(DataKeys.MATCH_NO_SHOW, noShow);
		data.putFloat(DataKeys.MATCH_AUTO_LOC_X, xPos);
		data.putFloat(DataKeys.MATCH_AUTO_LOC_Y, yPos);
		data.putInteger(DataKeys.MATCH_AUTO_STARTING_DISCS, loadingDiscs);
	}

	@Override
	public void loadInformation(DataCache data) {
		noShow = data.getBoolean(DataKeys.MATCH_NO_SHOW, false);
		xPos = data.getFloat(DataKeys.MATCH_AUTO_LOC_X, 0);
		yPos = data.getFloat(DataKeys.MATCH_AUTO_LOC_Y, 0);
		loadingDiscs = data.getInteger(DataKeys.MATCH_AUTO_STARTING_DISCS, 2);
	}

	@Override
	public boolean needsKeyboard() {
		return false;
	}
}
