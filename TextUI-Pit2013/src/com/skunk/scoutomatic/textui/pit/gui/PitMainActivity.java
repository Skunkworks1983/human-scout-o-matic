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

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.pi.scoutomatic.lib.data.Pair;
import com.pi.scoutomatic.lib.net.FutureProcessor;
import com.pi.scoutomatic.lib.net.NetworkSettingsKeys;
import com.skunk.scoutomatic.textui.gui.ScoutingActivity;
import com.skunk.scoutomatic.textui.gui.frag.INamedTabFragment;
import com.skunk.scoutomatic.textui.gui.frag.dummy.SubmitFragment;
import com.skunk.scoutomatic.textui.gui.frag.support.NetworkManagementFragment;
import com.skunk.scoutomatic.textui.gui.frag.support.SettingsFragment;
import com.skunk.scoutomatic.textui.pit.DataKeys;
import com.skunk.scoutomatic.textui.pit.R;
import com.skunk.scoutomatic.textui.pit.gui.frag.WelcomeFragment;
import com.skunk.scoutomatic.textui.pit.net.PitBackendInterface;
import com.skunk.scoutomatic.textui.pit.net.ScoutableRobot;

public class PitMainActivity extends ScoutingActivity {
	private PitBackendInterface backend;

	public void onCreate(Bundle savedInstanceState) {
		if (backend == null) {
			backend = new PitBackendInterface(this);
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

	public PitBackendInterface getBackend() {
		return backend;
	}

	@Override
	protected void onSubmit() {
		backend.pushRobotInformation(getDataHeap());
	}

	@Override
	protected void onTabChanged(Class<? extends INamedTabFragment> from,
			Class<? extends INamedTabFragment> to) {
		if (to == WelcomeFragment.class
				&& (from == SubmitFragment.class || from == null)) {
			// Cache the persistent data values
			String competition = getDataHeap().getString(
					DataKeys.ROBOT_COMPETITION,
					PreferenceManager.getDefaultSharedPreferences(
							getApplicationContext()).getString(
							NetworkSettingsKeys.EVENT_ID,
							NetworkSettingsKeys.DEFAULT_EVENT_ID));
			String scoutName = getDataHeap()
					.getString(DataKeys.ROBOT_SCOUT, "");

			// Clear and commit
			commitDataHeap(new Pair(null, null), new Pair(
					DataKeys.ROBOT_COMPETITION, competition), new Pair(
					DataKeys.ROBOT_SCOUT, scoutName));

			backend.pollScoutingQueue(new FutureProcessor<ScoutableRobot>() {
				@Override
				public void run(ScoutableRobot mm) {
					Log.d("NET", "Scouting robot: " + mm.getRobotID());
					PitMainActivity.super.commitDataHeap(new Pair(
							DataKeys.ROBOT_TEAM, Integer.valueOf(mm
									.getRobotID())));
				}
			}, false);
		}
	}
}