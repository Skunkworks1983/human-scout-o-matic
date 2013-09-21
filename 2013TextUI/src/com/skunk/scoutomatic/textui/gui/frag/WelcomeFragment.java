package com.skunk.scoutomatic.textui.gui.frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skunk.scoutomatic.textui.R;

/**
 * Created on: Sep 20, 2013
 * 
 * @author Westin Miller
 */
public class WelcomeFragment extends NamedTabFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.welcome_fragment, container, false);
	}

	@Override
	public String getName() {
		return "Welcome";
	}

	@Override
	public Class<? extends NamedTabFragment> getNext() {
		return AutoLocFragment.class;
	}

	@Override
	public Class<? extends NamedTabFragment> getPrevious() {
		return null;
	}
}
