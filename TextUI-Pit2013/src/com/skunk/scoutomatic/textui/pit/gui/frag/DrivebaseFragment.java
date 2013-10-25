package com.skunk.scoutomatic.textui.pit.gui.frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skunk.scoutomatic.lib.data.DataCache;
import com.skunk.scoutomatic.lib.gui.frag.INamedTabFragment;
import com.skunk.scoutomatic.lib.gui.frag.NamedTabFragmentImpl;
import com.skunk.scoutomatic.textui.pit.DataKeys;
import com.skunk.scoutomatic.textui.pit.R;

public class DrivebaseFragment extends NamedTabFragmentImpl {

	private int cimCount = 0;
	private int wheelCount = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater
				.inflate(R.layout.drivebase_fragment, container, false);
		return v;
	}

	@Override
	public String getName() {
		return "Drivebase";
	}

	@Override
	public Class<? extends INamedTabFragment> getNext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<? extends INamedTabFragment> getPrevious() {
		// TODO Auto-generated method stub
		return WelcomeFragment.class;
	}

	@Override
	public void storeInformation(DataCache data) {
		data.putInteger(DataKeys.DRIVEBASE_WHEELS, wheelCount);
		data.putInteger(DataKeys.DRIVEBASE_CIMS, cimCount);
	}

	@Override
	public void loadInformation(DataCache data) {
		wheelCount = data.getInteger(DataKeys.DRIVEBASE_WHEELS, 6);
		cimCount = data.getInteger(DataKeys.DRIVEBASE_CIMS, 2);
	}

	@Override
	public boolean needsKeyboard() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void updateContents() {
		super.setState(R.id.drive4Wheel, wheelCount == 4);
		super.setState(R.id.drive6Wheel, wheelCount == 6);
		super.setState(R.id.drive8Wheel, wheelCount == 8);
		if (wheelCount != 4 && wheelCount != 6 && wheelCount != 8) {
			super.setState(R.id.driveOtherWheel, true);
		}
		// TODO
	}

}
