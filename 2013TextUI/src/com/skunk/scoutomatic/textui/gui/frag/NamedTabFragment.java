package com.skunk.scoutomatic.textui.gui.frag;

import android.support.v4.app.Fragment;

/**
 * Created on: Sep 20, 2013
 * 
 * @author "Westin Miller"
 * 
 */
public abstract class NamedTabFragment extends Fragment {
	public abstract String getName();
	public abstract Class<? extends NamedTabFragment> getNext();
	public abstract Class<? extends NamedTabFragment> getPrevious();
}
