package com.skunk.scoutomatic.textui.gui.frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skunk.scoutomatic.textui.R;

/**
 * Created on: Sep 20, 2013
 * 
 * @author Westin Miller
 */
public class AutoLocFragment extends NamedTabFragment implements
		OnTouchListener, OnClickListener {
	private float xPos = 0, yPos = 0;
	private boolean noShow = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.auto_loc_fragment, container, false);
		View autoSheet = v.findViewById(R.id.autoScoutSheet);
		if (autoSheet != null) {
			autoSheet.setOnTouchListener(this);
		}
		View noShow = v.findViewById(R.id.matchNoShow);
		if (noShow != null) {
			noShow.setOnClickListener(this);
		}
		return v;
	}

	@Override
	public String getName() {
		return "Autonomous Location";
	}

	@Override
	public Class<? extends NamedTabFragment> getNext() {
		return AutoScoreFragment.class;
	}

	@Override
	public Class<? extends NamedTabFragment> getPrevious() {
		return WelcomeFragment.class;
	}

	@Override
	public void onResume() {
		super.onResume();
		updateLocation();	// TODO Restore the marker properly
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			switch (v.getId()) {
			case R.id.autoScoutSheet:
				xPos = (event.getX() / v.getWidth()) * 100;
				yPos = (event.getY() / v.getHeight()) * 100;
				updateLocation();
				break;
			}
			return true;
		}
		return true;
	}

	private void updateLocation() {
		View check = getView().findViewById(R.id.matchNoShow);
		if (check != null && check instanceof CheckBox) {
			((CheckBox) check).setChecked(noShow);
		}
		if (xPos == 0.0f && yPos == 0.0f) {
			return;
		}
		View v = getView().findViewById(R.id.autoScoutSheet);
		View vv = getView().findViewById(R.id.autoFirstRobot);
		View textView = getView().findViewById(R.id.autoRobotLocation);
		if (textView != null && textView instanceof TextView) {
			((TextView) textView)
					.setText(xPos
							+ ","
							+ yPos
							+ ","
							+ ((RelativeLayout.LayoutParams) vv
									.getLayoutParams()).leftMargin);
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
		}
	}
}
