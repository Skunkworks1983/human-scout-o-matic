package com.skunk.scoutomatic.textui.gui.frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skunk.scoutomatic.textui.R;

/**
 * Created on: Sep 20, 2013
 * 
 * @author Westin Miller
 */
public class TeleShootLocFragment extends NamedTabFragment implements
		OnTouchListener {
	private float xPos = 0, yPos = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.tele_shoot_loc_fragment, container,
				false);
		View autoSheet = v.findViewById(R.id.telePyramidSheet);
		if (autoSheet != null) {
			autoSheet.setOnTouchListener(this);
		}
		return v;
	}

	@Override
	public String getName() {
		return "Shooting Location";
	}

	@Override
	public Class<? extends NamedTabFragment> getNext() {
		return ReviewFoulsSkillsFragment.class;
	}

	@Override
	public Class<? extends NamedTabFragment> getPrevious() {
		return TeleClimbFragment.class;
	}

	@Override
	public void onResume() {
		super.onResume();
		updateLocation(); // TODO Restore the marker properly
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			switch (v.getId()) {
			case R.id.telePyramidSheet:
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
		if (xPos == 0.0f && yPos == 0.0f) {
			return;
		}
		View v = getView().findViewById(R.id.telePyramidSheet);
		View vv = getView().findViewById(R.id.teleFirstRobot);
		View textView = getView().findViewById(R.id.teleShootLocation);
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
}
