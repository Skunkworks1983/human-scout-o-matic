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
	private int wheelType = 0;
	private String wheelSurfaceExt = null;
	private int wheelSurface = 0;
	private int shiftTrans = 0;
	private int width = 0;
	private int length = 0;

	//TODO: Better place for this stuff?
	public static final int REGULAR = 0;

	public static final int OMNI = 1;
	public static final int MECHANUM = 2;

	public static final int RUBBER = 1;
	public static final int NA = 2;

	public static final int T_NA = 0;
	public static final int T_YES = 1;
	public static final int T_NO = 2;
	
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
		return FeaturesFragment.class;
	}

	@Override
	public Class<? extends INamedTabFragment> getPrevious() {
		return WelcomeFragment.class;
	}

	@Override
	public void storeInformation(DataCache data) {
		data.putInteger(DataKeys.DRIVEBASE_WHEEL_NUM, wheelCount);
		data.putInteger(DataKeys.DRIVEBASE_CIMS, cimCount);
		data.putInteger(DataKeys.DRIVEBASE_WHEEL_TYPE, wheelType);
		if (wheelSurfaceExt == null) {
			data.putInteger(DataKeys.DRIVEBASE_WHEEL_SURFACE, wheelSurface);
		}

		else {
			data.putString(DataKeys.DRIVEBASE_WHEEL_SURFACE, wheelSurfaceExt);
		}
		data.putInteger(DataKeys.DRIVEBASE_SHIFT_TRANS, shiftTrans);
		data.putInteger(DataKeys.DRIVEBASE_WIDTH, width);
		data.putInteger(DataKeys.DRIVEBASE_LENGTH, length);
	}

	@Override
	public void loadInformation(DataCache data) { // TODO: More reasonable
													// guesses
		wheelCount = data.getInteger(DataKeys.DRIVEBASE_WHEEL_NUM, 6);
		cimCount = data.getInteger(DataKeys.DRIVEBASE_CIMS, 2);
		wheelType = data.getInteger(DataKeys.DRIVEBASE_WHEEL_TYPE, REGULAR);
		wheelSurface = data.getInteger(DataKeys.DRIVEBASE_WHEEL_SURFACE,
				REGULAR);
		wheelSurfaceExt = data.getString(DataKeys.DRIVEBASE_WHEEL_SURFACE, null);
		shiftTrans = data.getInteger(DataKeys.DRIVEBASE_SHIFT_TRANS, T_NA);
		width = data.getInteger(DataKeys.DRIVEBASE_WIDTH, 36);
		length = data.getInteger(DataKeys.DRIVEBASE_LENGTH, 36);
	}

	@Override
	public boolean needsKeyboard() {
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

		super.setState(R.id.driveRegType, wheelType == REGULAR);
		super.setState(R.id.driveOmni, wheelType == OMNI);
		super.setState(R.id.driveMechanum, wheelType == MECHANUM);

		super.setState(R.id.driveRegSurface, wheelSurface == REGULAR);
		super.setState(R.id.driveRubber, wheelSurface == RUBBER);
		super.setState(R.id.driveNASurface, wheelSurface == NA);
		if (!super.getState(R.id.driveRegSurface)
				&& !super.getState(R.id.driveRubber)
				&& !super.getState(R.id.driveNASurface)) {
			super.setState(R.id.driveOtherMaterial, true);
			try {
				super.setTextContents(R.id.driveOtherMaterialInput,
						wheelSurfaceExt);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.setState(R.id.driveShiftTransTrue, shiftTrans == T_NA);
		super.setState(R.id.driveShiftTransFalse, shiftTrans == T_YES);
		super.setState(R.id.driveShiftTransNA, shiftTrans == T_NO);
	}

	@Override
	public void onTextChanged(CharSequence sq, int arg1, int arg2, int arg3) {
		String s = sq.toString();
		if (getTextContents(R.id.driveOtherWheelInput).equalsIgnoreCase(s)) {
			try {
				wheelCount = Integer.valueOf(s);
			} catch (NumberFormatException ex) {
			}
		} else if (getTextContents(R.id.driveOtherMaterial).equalsIgnoreCase(s)) {
			wheelSurfaceExt = s;
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
		switch (v.getId()) {
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
			wheelSurface = REGULAR;
			break;

		case R.id.driveOmni:
			wheelType = OMNI;
			break;

		case R.id.driveMechanum:
			wheelType = MECHANUM;
			break;

		case R.id.driveRegSurface:
			wheelSurface = REGULAR;
			break;

		case R.id.driveRubber:
			wheelSurface = RUBBER;
			break;

		case R.id.driveNASurface:
			wheelType = NA;
			break;

		case R.id.driveShiftTransTrue:
			shiftTrans = T_NA;
			break;

		case R.id.driveShiftTransFalse:
			shiftTrans = T_YES;
			break;

		case R.id.driveShiftTransNA:
			shiftTrans = T_NO;
			break;
		}
		super.postUpdate();
	}
}
