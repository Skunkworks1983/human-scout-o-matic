package com.skunk.scoutomatic.textui.pit.gui.frag;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skunk.scoutomatic.lib.data.DataCache;
import com.skunk.scoutomatic.lib.gui.frag.INamedTabFragment;
import com.skunk.scoutomatic.lib.gui.frag.NamedTabFragmentImpl;
import com.skunk.scoutomatic.textui.pit.DataKeys;
import com.skunk.scoutomatic.textui.pit.R;

public class DrivebaseFragment extends NamedTabFragmentImpl implements
		TextWatcher  {
	private int cimCount = 0;
	private int wheelCount = 0;
	private String wheelType = "";
	private String wheelSurface = "";
	private boolean shiftTrans = false;
	private int width = 0;
	private int length = 0;

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
		data.putInteger(DataKeys.DRIVEBASE_WHEEL_NUM, wheelCount);
		data.putInteger(DataKeys.DRIVEBASE_CIMS, cimCount);
		data.putString(DataKeys.DRIVEBASE_WHEEL_TYPE, wheelType);
		data.putString(DataKeys.DRIVEBASE_WHEEL_SURFACE, wheelSurface);
		data.putBoolean(DataKeys.DRIVEBASE_SHIFT_TRANS, shiftTrans);
		data.putInteger(DataKeys.DRIVEBASE_WIDTH, width);
		data.putInteger(DataKeys.DRIVEBASE_LENGTH, length);
	}

	@Override
	public void loadInformation(DataCache data) { // TODO: More reasonable
													// guesses
		wheelCount = data.getInteger(DataKeys.DRIVEBASE_WHEEL_NUM, 6);
		cimCount = data.getInteger(DataKeys.DRIVEBASE_CIMS, 2);
		wheelType = data.getString(DataKeys.DRIVEBASE_WHEEL_TYPE, "Regular");
		wheelSurface = data.getString(DataKeys.DRIVEBASE_WHEEL_SURFACE,
				"Reuglar");
		shiftTrans = data.getBoolean(DataKeys.DRIVEBASE_SHIFT_TRANS, false);
		width = data.getInteger(DataKeys.DRIVEBASE_WIDTH, 36);
		length = data.getInteger(DataKeys.DRIVEBASE_LENGTH, 36);
	}

	@Override
	public boolean needsKeyboard() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * Order: Dimensions Wheel Number Wheel Type Wheel Surface Shiftable
	 * Transmission Drive Base Motors
	 */
	@Override
	protected void updateContents() {
		super.setTextContents(R.id.driveWidth,
				width > 0 ? Integer.toString(width) : "");
		super.setTextContents(R.id.driveLength,
				length > 0 ? Integer.toString(length) : "");

		super.setState(R.id.drive4Wheel, wheelCount == 4);
		super.setState(R.id.drive6Wheel, wheelCount == 6);
		super.setState(R.id.drive8Wheel, wheelCount == 8);
		if (wheelCount != 4 && wheelCount != 6 && wheelCount != 8) {
			super.setState(R.id.driveOtherWheel, true);
			try {
				super.setTextContents(R.id.driveOtherWheelInput,
						wheelCount > 0 ? Integer.toString(wheelCount) : "");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		super.setState(R.id.driveRegType, wheelType == "Regular");
		super.setState(R.id.driveOmni, wheelType == "Omni");
		super.setState(R.id.driveMechanum, wheelType == "Mechanum");

		super.setState(R.id.driveRegSurface, wheelSurface == "Regular");
		super.setState(R.id.driveRubber, wheelSurface == "Rubber");
		super.setState(R.id.driveNASurface, wheelSurface == "NA");
		if (wheelSurface != "Regular" && wheelSurface != "Rubber"
				&& wheelSurface != "NA") {
			super.setState(R.id.driveOtherMaterial, true);
			try {
				super.setTextContents(R.id.driveOtherMaterialInput, wheelSurface);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//asdiuflasfsdjf
	@Override
	public void onTextChanged(CharSequence sq, int arg1, int arg2, int arg3) {
		String s = sq.toString();
		if (getTextContents(R.id.driveOtherWheelInput).equalsIgnoreCase(s)) {
			try {
				wheelCount = Integer.valueOf(s);
			} catch (NumberFormatException ex) {
			}
		} else if (getTextContents(R.id.driveOtherMaterial).equalsIgnoreCase(s)) {
			wheelSurface = s;
		} else if (getTextContents(R.id.driveWidth).equalsIgnoreCase(s)) {
			try {
				width = Integer.valueOf(s);
			} catch (NumberFormatException ex) {
			}
		} else if (getTextContents(R.id.driveLength).equalsIgnoreCase(s)) {
			try {
				length = Integer.valueOf(s);
			} catch (NumberFormatException ex) {
			}
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}
}
