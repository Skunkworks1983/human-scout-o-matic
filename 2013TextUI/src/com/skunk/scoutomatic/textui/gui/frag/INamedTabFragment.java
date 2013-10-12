package com.skunk.scoutomatic.textui.gui.frag;

import android.view.View;

import com.skunk.scoutomatic.textui.DataCache;

/**
 * Created on: Oct 11, 2013
 * 
 * @author "Westin Miller"
 * 
 */
public interface INamedTabFragment {
	public int getColor();

	public String getName();

	public Class<? extends INamedTabFragment> getNext();

	public Class<? extends INamedTabFragment> getPrevious();

	public void storeInformation(DataCache data);

	public void loadInformation(DataCache data);

	public boolean needsKeyboard();

	public View getView();

	public void postUpdate();
}
