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
public class AutoLocFragment extends NamedTabFragment implements
		OnTouchListener {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.auto_loc_fragment, container, false);
		View autoSheet = v.findViewById(R.id.autoScoutSheet);
		autoSheet.setOnTouchListener(this);
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
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			switch (v.getId()) {
			case R.id.autoScoutSheet:
				float xPos = (event.getX() / v.getWidth()) * 100;
				float yPos = (event.getY() / v.getHeight()) * 100;
				View textView = getView().findViewById(R.id.autoRobotLocation);
				if (textView != null && textView instanceof TextView) {
					((TextView) textView).setText(xPos + ","
							+ yPos);
				}
				View vv = getView().findViewById(R.id.autoFirstRobot);
				if (vv != null) {
					vv.setVisibility(View.VISIBLE);
					((RelativeLayout.LayoutParams) vv.getLayoutParams())
							.setMargins((int) event.getX()
									- (vv.getWidth() / 2), (int) event.getY()
									- (vv.getHeight() / 2), 0, 0);
					vv.requestLayout();
				}
				break;
			}
			return true;
		}
		return true;
	}
}
