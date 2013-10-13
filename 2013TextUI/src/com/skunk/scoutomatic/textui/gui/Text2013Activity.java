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

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.pi.scoutomatic.lib.data.Pair;
import com.pi.scoutomatic.lib.net.BackendInterface;
import com.pi.scoutomatic.lib.net.FutureProcessor;
import com.pi.scoutomatic.lib.net.NetworkSettingsKeys;
import com.skunk.scoutomatic.textui.DataKeys;
import com.skunk.scoutomatic.textui.R;
import com.skunk.scoutomatic.textui.gui.frag.INamedTabFragment;
import com.skunk.scoutomatic.textui.gui.frag.WelcomeFragment;
import com.skunk.scoutomatic.textui.gui.frag.dummy.SubmitFragment;
import com.skunk.scoutomatic.textui.gui.frag.support.NetworkManagementFragment;
import com.skunk.scoutomatic.textui.gui.frag.support.SettingsFragment;
import com.skunk.scoutomatic.textui.net.ScoutableMatch;
import com.skunk.scoutomatic.textui.net.Text2013BackendInterface;

public class Text2013Activity extends ScoutingActivity {
	private Text2013BackendInterface backend;

	public void onCreate(Bundle savedInstanceState) {
		if (backend == null) {
			backend = new Text2013BackendInterface(this);
		}
		super.onCreate(savedInstanceState, WelcomeFragment.class);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.action_bar, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuConfig:
			setFragment(SettingsFragment.class);
			return true;
		case R.id.menuNetState:
			// Do stuff
			setFragment(NetworkManagementFragment.class);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public BackendInterface getBackend() {
		return backend;
	}

	@Override
	protected void onSubmit() {
		backend.pushMatchInformation(getDataHeap());
	}

	@Override
	protected void onTabChanged(Class<? extends INamedTabFragment> from,
			Class<? extends INamedTabFragment> to) {
		if (to == WelcomeFragment.class
				&& (from == SubmitFragment.class || from == null)) {
			// Cache the persistent data values
			String competition = getDataHeap().getString(
					DataKeys.MATCH_COMPETITION,
					PreferenceManager.getDefaultSharedPreferences(
							getApplicationContext()).getString(
							NetworkSettingsKeys.EVENT_ID,
							NetworkSettingsKeys.DEFAULT_EVENT_ID));
			String scoutName = getDataHeap()
					.getString(DataKeys.MATCH_SCOUT, "");
			int nextMatch = getDataHeap().getInteger(DataKeys.MATCH_NUMBER, 0) + 1;

			// Clear and commit
			commitDataHeap(new Pair(null, null), new Pair(
					DataKeys.MATCH_COMPETITION, competition), new Pair(
					DataKeys.MATCH_NUMBER, Integer.valueOf(nextMatch)),
					new Pair(DataKeys.MATCH_SCOUT, scoutName));

			backend.pollScoutingQueue(new FutureProcessor<ScoutableMatch>() {
				@Override
				public void run(ScoutableMatch mm) {
					Log.d("NET", "Scouting match: " + mm.getMatchID());
					Text2013Activity.super.commitDataHeap(new Pair(
							DataKeys.MATCH_NUMBER, mm.getMatchID()), new Pair(
							DataKeys.MATCH_TEAM, mm.getRobotID()));
				}
			}, false);
		}
	}
}