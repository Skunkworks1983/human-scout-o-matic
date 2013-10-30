package com.skunk.scoutomatic.textui.pit.gui.frag;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

import com.skunk.scoutomatic.lib.data.DataCache;
import com.skunk.scoutomatic.lib.gui.frag.INamedTabFragment;
import com.skunk.scoutomatic.lib.gui.frag.NamedTabFragmentImpl;
import com.skunk.scoutomatic.textui.pit.DataKeys;
import com.skunk.scoutomatic.textui.pit.R;

public class DrivebaseFragment extends NamedTabFragmentImpl implements
		TextWatcher, OnClickListener {
	private int cimCount = 0;
	private int wheelCount = 0;
	private String wheelType = "";
	private String wheelSurface = "";
	private int shiftTrans = -1;
	private int width = 0;
	private int length = 0;

	private final void registerClickListener(View v, int id) {
		View vv = v.findViewById(id);
		if (vv != null) {
			vv.setOnClickListener(this);
		}
	}

	private final void registerKeyChange(View v, int id) {
		View vv = v.findViewById(id);
		if (vv != null) {
			((EditText) vv).addTextChangedListener(this);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater
				.inflate(R.layout.drivebase_fragment, container, false);
		registerClickListener(v, R.id.drive2CIM);
		registerKeyChange(v, R.id.driveOtherMaterialInput);
		
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
		data.putInteger(DataKeys.DRIVEBASE_SHIFT_TRANS, shiftTrans);
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
		shiftTrans = data.getInteger(DataKeys.DRIVEBASE_SHIFT_TRANS, 1);
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

		super.setState(R.id.driveRegType, wheelType.equalsIgnoreCase("regular"));
		super.setState(R.id.driveOmni, wheelType.equalsIgnoreCase("omni"));
		super.setState(R.id.driveMechanum,
				wheelType.equalsIgnoreCase("mechanum"));

		super.setState(R.id.driveRegSurface,
				wheelSurface.equalsIgnoreCase("regular"));
		super.setState(R.id.driveRubber,
				wheelSurface.equalsIgnoreCase("rubber"));
		super.setState(R.id.driveNASurface, wheelSurface.equalsIgnoreCase("na"));
		if (!super.getState(R.id.driveRegSurface)
				&& !super.getState(R.id.driveRubber)
				&& !super.getState(R.id.driveNASurface)) {
			super.setState(R.id.driveOtherMaterial, true);
			try {
				super.setTextContents(R.id.driveOtherMaterialInput,
						wheelSurface);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.setState(R.id.driveShiftTransTrue, shiftTrans == 0);
		super.setState(R.id.driveShiftTransFalse, shiftTrans == 1);
		super.setState(R.id.driveShiftTransNA, shiftTrans == 2);
	}

	// asdiuflasfsdjf
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
		super.postUpdate();
	}

	@Override
	public void afterTextChanged(Editable s) {
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.drive2CIM:
			cimCount = 2;
			break;
		
		case R.id.drive4CIM:
			cimCount = 4;
			break;
			
		case R.id.drive4Wheel:
			wheelCount = 4;
			break;
		
		case R.id.drive6Wheel:
			wheelCount = 6;
			break;
			
		case R.id.drive8Wheel:
			wheelCount = 8;
			break;
		
		case R.id.driveRegType:
			wheelSurface = "regular";
			break;
		
		case R.id.driveMechanum:
			wheelType = "mechanum";
			break;
		
		case R.id.driveOmni:
			wheelType = "omni";
			break;
		
		case R.id.driveRegSurface:
			wheelSurface = "regular";
			break;
			
		case R.id.driveRubber:
			wheelSurface = "rubber";
			break;

		case R.id.driveNASurface:
			wheelType = "na";
			break;
		
		case R.id.driveShiftTransTrue:
			shiftTrans = 0;
			break;
		
		case R.id.driveShiftTransFalse:
			shiftTrans = 1;
			break;
			
		case R.id.driveShiftTransNA:
			shiftTrans = 2;
			break;
		}
		super.postUpdate();
	}
}
