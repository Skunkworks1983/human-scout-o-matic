/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.skunk.scoutomatic.textui.pit.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.skunk.scoutomatic.textui.pit.DataCache;
import com.skunk.scoutomatic.textui.pit.DataKeys;
import com.skunk.scoutomatic.textui.pit.R;
import com.skunk.scoutomatic.textui.pit.gui.frag.NamedTabFragment;
import com.skunk.scoutomatic.textui.pit.gui.frag.SubmitFragment;
import com.skunk.scoutomatic.textui.pit.gui.frag.WelcomeFragment;
import com.skunk.scoutomatic.textui.pit.net.BackendInterface;

public class MainActivity extends FragmentActivity implements
		OnLayoutChangeListener {
	private Map<Class<? extends NamedTabFragment>, NamedTabFragment> fragments = new HashMap<Class<? extends NamedTabFragment>, NamedTabFragment>();
	private NamedTabFragment currentFragment = new WelcomeFragment();
	private BackendInterface backend;
	private Stack<Class<? extends NamedTabFragment>> history = new Stack<Class<? extends NamedTabFragment>>();

	private DataCache dataHeap = new DataCache(new HashMap<String, Object>());

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		View v = findViewById(R.id.main_fragment_scroll);
		v.addOnLayoutChangeListener(this);

		backend = new BackendInterface();
		dataHeap.putString(DataKeys.ROBOT_COMPETITION,
				BackendInterface.EVENT_ID);
		if (findViewById(R.id.main_fragment_container) != null) {
			fragments.put(currentFragment.getClass(), currentFragment);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.main_fragment_container, currentFragment)
					.commit();
			setFragment(currentFragment.getClass());
		}
	}

	public void changeFragment(View v) {
		if (v.getId() == R.id.buttonLeft) {
			setFragment(currentFragment.getPrevious());
		}
		if (v.getId() == R.id.buttonRight) {
			setFragment(currentFragment.getNext());
		}
	}

	private void setFragment(Class<? extends NamedTabFragment> fragClass) {
		if (fragClass == null) {
			return;
		}
		try {
			boolean initWelcome = currentFragment == null
					|| currentFragment.getClass().equals(fragClass);
			if (fragClass == SubmitFragment.class) {
				submitData();
				initWelcome = true;
				fragClass = WelcomeFragment.class;
			}
			NamedTabFragment tab = fragments.get(fragClass);
			if (tab == null) {
				tab = fragClass.newInstance();
				fragments.put(fragClass, tab);
			}
			// Replace it
			if (currentFragment != null && !initWelcome) {
				currentFragment.storeInformation(dataHeap);

				// Hide keyboard if needed
				if (getCurrentFocus() != null && !tab.needsKeyboard()) {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(getCurrentFocus()
							.getWindowToken(), 0);
				}
			}
			if (currentFragment != tab && currentFragment != null) {
				history.add(currentFragment.getClass());
			}
			currentFragment = tab;
			currentFragment.loadInformation(dataHeap);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.main_fragment_container, currentFragment)
					.commit();

			// Update NAV
			View label = findViewById(R.id.fragmentCurrent);
			View left = findViewById(R.id.buttonLeft);
			View right = findViewById(R.id.buttonRight);
			if (label != null && label instanceof TextView) {
				((TextView) label).setText(tab.getName());
			}
			if (left != null) {
				left.setVisibility(tab.getPrevious() != null ? View.VISIBLE
						: View.INVISIBLE);
			}
			if (right != null) {
				right.setVisibility(tab.getNext() != null ? View.VISIBLE
						: View.INVISIBLE);
			}
			if (initWelcome) {
				proceedToNextRobot();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void proceedToNextRobot() {
		// TODO Auto-generated method stub
	}

	private void submitData() {
		// TODO Auto-generated method stub

		// Now clear the cache except for small stuff
		String compID = dataHeap.getString(DataKeys.ROBOT_COMPETITION, "");
		String scoutName = dataHeap.getString(DataKeys.ROBOT_SCOUT, "");
		dataHeap.getData().clear();
		dataHeap.putString(DataKeys.ROBOT_COMPETITION, compID);
		dataHeap.putString(DataKeys.ROBOT_SCOUT, scoutName);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && history.size() > 0) {
			int size = history.size();
			setFragment(history.pop());
			if (size == history.size()) {
				// Remove the new one we added
				history.pop();
			}
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onLayoutChange(View v, int left, final int top, int right,
			final int bottom, int oldLeft, int oldTop, int oldRight,
			int oldBottom) {// v.invalidate();
		if (currentFragment.getView() != null) {
			final View fragView = currentFragment.getView();
			if (fragView.getMinimumHeight() != (bottom - top)
					|| fragView.getHeight() < fragView.getMinimumHeight()) {
				fragView.post(new Runnable() {
					public void run() {
						fragView.setMinimumHeight(bottom - top);

						if (fragView.getHeight() < fragView.getMinimumHeight()) {
							FrameLayout.LayoutParams pp = new FrameLayout.LayoutParams(
									fragView.getLayoutParams());
							pp.height = fragView.getMinimumHeight();
							fragView.setLayoutParams(pp);
						} else if (fragView.getHeight() > fragView
								.getMinimumHeight()) {
							FrameLayout.LayoutParams pp = new FrameLayout.LayoutParams(
									fragView.getLayoutParams());
							if (pp.height != FrameLayout.LayoutParams.MATCH_PARENT) {
								pp.height = FrameLayout.LayoutParams.WRAP_CONTENT;
							}
							fragView.setLayoutParams(pp);
						}

						fragView.requestLayout();
						System.out.println("MIN HEIGHT: " + (bottom - top));
					}
				});
			}
		}
	}
}