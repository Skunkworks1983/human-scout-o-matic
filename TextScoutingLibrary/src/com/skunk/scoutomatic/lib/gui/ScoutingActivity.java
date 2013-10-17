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
package com.skunk.scoutomatic.lib.gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skunk.scoutomatic.lib.R;
import com.skunk.scoutomatic.lib.data.DataCache;
import com.skunk.scoutomatic.lib.data.Pair;
import com.skunk.scoutomatic.lib.gui.frag.INamedTabFragment;
import com.skunk.scoutomatic.lib.gui.frag.dummy.GoBackFragment;
import com.skunk.scoutomatic.lib.gui.frag.dummy.SubmitFragment;
import com.skunk.scoutomatic.lib.net.BackendInterface;

public abstract class ScoutingActivity extends FragmentActivity implements
		OnLayoutChangeListener, MessageReciever {
	private Map<Class<? extends INamedTabFragment>, INamedTabFragment> fragments = new HashMap<Class<? extends INamedTabFragment>, INamedTabFragment>();
	private INamedTabFragment currentFragment;
	private Stack<Class<? extends INamedTabFragment>> history = new Stack<Class<? extends INamedTabFragment>>();

	private DataCache dataHeap = new DataCache(new HashMap<String, Object>());
	private Class<? extends INamedTabFragment> startingFragment;

	/**
	 * When this sees a null argument it will clear the heap.
	 * 
	 * @param commitables
	 */
	public void commitDataHeap(Pair... commitables) {
		if (currentFragment != null) {
			currentFragment.storeInformation(dataHeap);
		}
		for (int i = 0; i < commitables.length; i++) {
			Pair commit = commitables[i];
			if (commit == null || commit.getKey() == null
					|| commit.getValue() == null) {
				dataHeap.getData().clear();
			} else if (commit.getValue() instanceof Integer) {
				dataHeap.putInteger(commit.getKey(),
						((Integer) commit.getValue()).intValue());
			} else if (commit.getValue() instanceof Float) {
				dataHeap.putFloat(commit.getKey(),
						((Float) commit.getValue()).floatValue());
			} else if (commit.getValue() instanceof Long) {
				dataHeap.putLong(commit.getKey(),
						((Long) commit.getValue()).longValue());
			} else if (commit.getValue() instanceof Boolean) {
				dataHeap.putBoolean(commit.getKey(),
						((Boolean) commit.getValue()).booleanValue());
			} else if (commit.getValue() instanceof List) {
				dataHeap.putList(commit.getKey(), (List<?>) commit.getValue());
			} else if (commit.getValue() != null) {
				dataHeap.putString(commit.getKey(), commit.getValue()
						.toString());
			}
		}
		if (currentFragment != null) {
			currentFragment.loadInformation(dataHeap);
			currentFragment.postUpdate();
		}
	}

	/**
	 * Gets a DataHeap that can be read from.
	 * 
	 * Note: Don't WRITE to this data heap. It will cause unexpected stuff to
	 * happen.
	 * 
	 * @return the data heap
	 */
	public DataCache getDataHeap() {
		return dataHeap;
	}

	public void onCreate(Bundle savedInstanceState,
			Class<? extends INamedTabFragment> startingFragment) {
		this.startingFragment = startingFragment;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		View v = findViewById(R.id.main_fragment_scroll);
		v.addOnLayoutChangeListener(this);
		v = findViewById(R.id.mainActNav);
		v.setMinimumHeight(100);

		PreferenceManager.setDefaultValues(getApplicationContext(),
				R.xml.scouting_preferences, false);
		if (findViewById(R.id.main_fragment_container) != null) {
			setFragment(startingFragment);
		}
	}

	public final long getLongPreference(String key, long defacto) {
		try {
			String data = PreferenceManager.getDefaultSharedPreferences(
					getApplicationContext()).getString(key,
					String.valueOf(defacto));
			return Long.valueOf(data);
		} catch (Exception e) {
		}
		return defacto;
	}

	public void changeFragment(View v) {
		if (v.getId() == R.id.buttonLeft) {
			setFragment(currentFragment.getPrevious());
		}
		if (v.getId() == R.id.buttonRight) {
			setFragment(currentFragment.getNext());
		}
	}

	protected abstract void onSubmit();

	public void onResume() {
		super.onResume();
		getBackend().beginQueue();
	}

	public void onPause() {
		super.onPause();
		getBackend().shutdownQueue(100L);
	}

	protected void setFragment(Class<? extends INamedTabFragment> fragClass) {
		if (fragClass == null) {
			return;
		}
		try {
			if (currentFragment != null) {
				currentFragment.storeInformation(dataHeap);
			}

			if (fragClass == SubmitFragment.class) {
				onTabChanged(
						currentFragment != null ? currentFragment.getClass()
								: null, SubmitFragment.class);
				fragClass = startingFragment;
			} else if (fragClass == GoBackFragment.class) {
				onTabChanged(
						currentFragment != null ? currentFragment.getClass()
								: null, GoBackFragment.class);
				if (history.size() > 0) {
					int size = history.size();
					setFragment(history.pop());
					if (size == history.size()) {
						// Remove the new one we added
						history.pop();
					}
				} else {
					setFragment(startingFragment);
				}
				return;
			}

			INamedTabFragment tab = fragments.get(fragClass);
			if (tab == null) {
				tab = fragClass.newInstance();
				fragments.put(fragClass, tab);
			}
			// Replace it
			if (currentFragment != null) {
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
			INamedTabFragment lastFragment = currentFragment;
			currentFragment = tab;
			currentFragment.loadInformation(dataHeap);

			// Recolor
			findViewById(R.id.main_act).setBackgroundColor(
					currentFragment.getColor());

			getFragmentManager()
					.beginTransaction()
					.replace(R.id.main_fragment_container,
							(Fragment) currentFragment).commit();

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
			onTabChanged(lastFragment != null ? lastFragment.getClass() : null,
					currentFragment != null ? currentFragment.getClass() : null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected abstract void onTabChanged(
			Class<? extends INamedTabFragment> from,
			Class<? extends INamedTabFragment> to);

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
					}
				});
			}
		}
	}

	@Override
	public void onMessage(final Class<?> src, final String message) {
		View vv = findViewById(R.id.main_act);
		if (vv != null) {
			vv.post(new Runnable() {
				public void run() {
					Log.i("TOAST", src.getSimpleName() + ": " + message);
					Toast.makeText(
							ScoutingActivity.super.getApplicationContext(),
							message, Toast.LENGTH_LONG).show();
				}
			});
		}
	}

	public abstract BackendInterface getBackend();
}