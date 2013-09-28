package com.skunk.scoutomatic.textui.pit.gui.frag;

import com.skunk.scoutomatic.textui.pit.DataCache;

/**
 * It's a dummy! (Maybe it will have network stuffs later) Created on: Sep 22,
 * 2013
 * 
 * @author "Westin Miller"
 * 
 */
public class SubmitFragment extends NamedTabFragment {
	@Override
	public String getName() {
		return null;
	}

	@Override
	public Class<? extends NamedTabFragment> getNext() {
		return null;
	}

	@Override
	public Class<? extends NamedTabFragment> getPrevious() {
		return null;
	}

	@Override
	public void storeInformation(DataCache data) {
	}

	@Override
	public void loadInformation(DataCache data) {
	}

	@Override
	protected void updateContents() {
	}

	@Override
	public boolean needsKeyboard() {
		return false;
	}

}
