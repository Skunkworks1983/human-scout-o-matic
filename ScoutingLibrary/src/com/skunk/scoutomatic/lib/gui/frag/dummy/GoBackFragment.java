package com.skunk.scoutomatic.lib.gui.frag.dummy;

import com.skunk.scoutomatic.lib.data.DataCache;
import com.skunk.scoutomatic.lib.gui.frag.NamedTabFragmentImpl;

/**
 * Created on: Oct 11, 2013
 * 
 * @author "Westin Miller"
 * 
 */
public class GoBackFragment extends NamedTabFragmentImpl {
	@Override
	public String getName() {
		return null;
	}

	@Override
	public Class<? extends NamedTabFragmentImpl> getNext() {
		return null;
	}

	@Override
	public Class<? extends NamedTabFragmentImpl> getPrevious() {
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
