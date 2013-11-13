package com.skunk.scoutomatic.textui.pit.gui.frag;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.RadioButton;

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
	private String wheelSurface = "";
	private int shiftTrans = 0;
	private int width = 0;
	private int length = 0;
	private boolean sketchyHack;
	// TODO: Better place for this stuff?
	public static final int REGULAR = 0;
	public static final String S_REGULAR = "regular";

	public static final int OMNI = 1;
	public static final int MECHANUM = 2;

	public static final String RUBBER = "rubber";
	public static final String S_NA = "na";
	public static final int NA = 0;

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
		registerClickListener(v, R.id.drive4CIM);
		registerClickListener(v, R.id.drive4Wheel);
		registerClickListener(v, R.id.drive6Wheel);
		registerClickListener(v, R.id.drive8Wheel);
		registerClickListener(v, R.id.driveRegType);
		registerClickListener(v, R.id.driveOmni);
		registerClickListener(v, R.id.driveMechanum);
		registerClickListener(v, R.id.driveRegSurface);
		registerClickListener(v, R.id.driveRubber);
		registerClickListener(v, R.id.driveNASurface);
		registerClickListener(v, R.id.driveShiftTransTrue);
		registerClickListener(v, R.id.driveShiftTransFalse);
		registerClickListener(v, R.id.driveShiftTransNA);
		registerKeyChange(v, R.id.driveOtherMaterialInput);
		registerKeyChange(v, R.id.driveOtherWheelInput);
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
		wheelType = data.getInteger(DataKeys.DRIVEBASE_WHEEL_TYPE, REGULAR);
		wheelSurface = data.getString(DataKeys.DRIVEBASE_WHEEL_SURFACE,
				S_REGULAR);
		shiftTrans = data.getInteger(DataKeys.DRIVEBASE_SHIFT_TRANS, T_NA);
		width = data.getInteger(DataKeys.DRIVEBASE_WIDTH, 36);
		length = data.getInteger(DataKeys.DRIVEBASE_LENGTH, 36);
	}

	@Override
	public boolean needsKeyboard() {
		return false;
	}

	@Override
	protected void updateContents() {
		if (!sketchyHack) {
			setTextContents(R.id.driveWidth,
					width > 0 ? Integer.toString(width) : "");
			setTextContents(R.id.driveLength,
					length > 0 ? Integer.toString(length) : "");
			setTextContents(R.id.driveOtherWheelInput,
					Integer.toString(wheelCount));
			setTextContents(R.id.driveOtherMaterialInput, wheelSurface);
		}

		setState(R.id.drive4Wheel, wheelCount == 4);
		setState(R.id.drive6Wheel, wheelCount == 6);
		setState(R.id.drive8Wheel, wheelCount == 8);

		setState(R.id.driveRegType, wheelType == REGULAR);
		setState(R.id.driveOmni, wheelType == OMNI);
		setState(R.id.driveMechanum, wheelType == MECHANUM);

		setState(R.id.driveRegSurface, wheelSurface.equalsIgnoreCase(S_REGULAR));
		setState(R.id.driveRubber, wheelSurface.equalsIgnoreCase(RUBBER));
		setState(R.id.driveNASurface, wheelSurface.equalsIgnoreCase(S_NA));
		setState(R.id.driveShiftTransTrue, shiftTrans == T_NA);
		setState(R.id.driveShiftTransFalse, shiftTrans == T_YES);
		setState(R.id.driveShiftTransNA, shiftTrans == T_NO);
	}

	@Override
	public void onTextChanged(CharSequence sq, int arg1, int arg2, int arg3) {
		String s = sq.toString();
		if (getTextContents(R.id.driveOtherWheelInput).equalsIgnoreCase(s)) {
			try {
				wheelCount = Integer.valueOf(s);
			} catch (NumberFormatException ex) {
			}
		} else if (getTextContents(R.id.driveOtherMaterialInput)
				.equalsIgnoreCase(s)) {
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
		sketchyHack = true;
		updateContents();
		sketchyHack = false;
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
			wheelType = REGULAR;
			break;

		case R.id.driveOmni:
			wheelType = OMNI;
			break;

		case R.id.driveMechanum:
			wheelType = MECHANUM;
			break;

		case R.id.driveRegSurface:
			wheelSurface = S_REGULAR;
			break;

		case R.id.driveRubber:
			wheelSurface = RUBBER;
			break;

		case R.id.driveNASurface:
			wheelSurface = S_NA;
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
		updateContents();
	}
}
