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
package com.skunk.scoutomatic.textui.gui;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.skunk.scoutomatic.textui.DataCache;
import com.skunk.scoutomatic.textui.R;
import com.skunk.scoutomatic.textui.gui.frag.NamedTabFragment;
import com.skunk.scoutomatic.textui.gui.frag.WelcomeFragment;

public class MainActivity extends FragmentActivity {
	private Map<Class<? extends NamedTabFragment>, NamedTabFragment> fragments = new HashMap<Class<? extends NamedTabFragment>, NamedTabFragment>();
	private NamedTabFragment currentFragment = new WelcomeFragment();

	private DataCache dataHeap = new DataCache(new HashMap<String, Object>());

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (findViewById(R.id.main_fragment_container) != null) {
			if (savedInstanceState != null) {
				return;
			}
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
			NamedTabFragment tab = fragments.get(fragClass);
			if (tab == null) {
				tab = fragClass.newInstance();
				fragments.put(fragClass, tab);
			}
			// Replace it
			currentFragment.storeInformation(dataHeap);
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
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}