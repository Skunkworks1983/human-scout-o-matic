package com.skunk.scoutomatic.lib.gui.frag.support;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.skunk.scoutomatic.lib.R;
import com.skunk.scoutomatic.lib.data.DataCache;
import com.skunk.scoutomatic.lib.gui.frag.INamedTabFragment;
import com.skunk.scoutomatic.lib.gui.frag.dummy.GoBackFragment;

/**
 * Created on: Oct 11, 2013
 * 
 * @author "Westin Miller"
 * 
 */
public class SettingsFragment extends PreferenceFragment implements
		INamedTabFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.scouting_preferences);
	}

	@Override
	public int getColor() {
		return 0x4499182C;
	}

	@Override
	public String getName() {
		return "Configuration";
	}

	@Override
	public Class<? extends INamedTabFragment> getNext() {
		return null;
	}

	@Override
	public Class<? extends INamedTabFragment> getPrevious() {
		return GoBackFragment.class;
	}

	@Override
	public void storeInformation(DataCache data) {
	}

	@Override
	public void loadInformation(DataCache data) {
	}

	@Override
	public boolean needsKeyboard() {
		return true;
	}

	@Override
	public void postUpdate() {
	}
}
