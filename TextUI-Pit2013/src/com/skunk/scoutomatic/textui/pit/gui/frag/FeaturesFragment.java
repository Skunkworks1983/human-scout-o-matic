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

public class FeaturesFragment extends NamedTabFragmentImpl implements
		TextWatcher, OnClickListener {
	private int shooterType = 0;
	private String shooterTypeExt = null;
	private int shooterRange = 0;
	private int loaderType = 0;
	private int loaderFrequency = 0;
	private int pyramidTier = 0;
	private int climbTime = 0;
	private int autoFunctions = 0;
	private int autoTime = 0;
	private int startingPos = 0;
	private boolean blocker = false;
	private int height = 0;

	public static final int NINETY_DEGREE = 1;
	public static final int LINEAR = 2;

	public static final int BELT = 1;
	public static final int SCOOP = 2;

	private final void registerClickListener(View v, int id) {
		View vv = v.findViewById(id);
		if (vv != null) {
			vv.setOnClickListener(this);
		}
	}

	private final void registerKeyChange(View v, int id) {
		View vv = v.findViewById(id);
		if (vv != null && vv instanceof EditText) {
			((EditText) vv).addTextChangedListener(this);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.feature_fragment, container, false);
		registerClickListener(v, R.id.featureShooter90);
		registerKeyChange(v, R.id.featureShooterOther);

		return v;
	}

	@Override
	public String getName() {
		return "Features";
	}

	@Override
	public Class<? extends INamedTabFragment> getNext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<? extends INamedTabFragment> getPrevious() {
		// TODO Auto-generated method stub
		return DrivebaseFragment.class;
	}

	@Override
	public void storeInformation(DataCache data) {
		data.putInteger(DataKeys.FEATURES_AUTO_FUNC, autoFunctions);
		data.putInteger(DataKeys.FEATURES_AUTO_TIME, autoTime);
		data.putBoolean(DataKeys.FEATURES_BLOCKER, blocker);
		data.putInteger(DataKeys.FEATURES_SHOOTER_RANGE, shooterRange);
		if (shooterTypeExt == null) {
			data.putInteger(DataKeys.FEATURES_SHOOTER_TYPE, shooterType);
		} else {
			data.putString(DataKeys.FEATURES_SHOOTER_TYPE, shooterTypeExt);
		}
		data.putInteger(DataKeys.FEATURES_HEIGHT, height);
		data.putInteger(DataKeys.FEATURES_PYRAMID_TIER, pyramidTier);
		data.putInteger(DataKeys.FEATURES_STARTING_POS, startingPos);
		data.putInteger(DataKeys.FEATURES_LOADER_FREQ, loaderFrequency);
		data.putInteger(DataKeys.FEATURES_LOADER_TYPE, loaderType);
		data.putInteger(DataKeys.FEATURES_CLIMB_TIME, climbTime);
	}

	@Override
	public void loadInformation(DataCache data) { // TODO: More reasonable
													// guesses

		shooterType = data.getInteger(DataKeys.FEATURES_SHOOTER_TYPE,
				NINETY_DEGREE);
		shooterTypeExt = data.getString(DataKeys.FEATURES_SHOOTER_TYPE, null);
		shooterRange = data.getInteger(DataKeys.FEATURES_SHOOTER_RANGE, 0);
		loaderType = data.getInteger(DataKeys.FEATURES_LOADER_TYPE, BELT);
		loaderFrequency = data.getInteger(DataKeys.FEATURES_LOADER_FREQ, 0);
		pyramidTier = data.getInteger(DataKeys.FEATURES_PYRAMID_TIER, 0);
		climbTime = data.getInteger(DataKeys.FEATURES_CLIMB_TIME, 0);
		autoFunctions = data.getInteger(DataKeys.FEATURES_AUTO_FUNC, 0);
		autoTime = data.getInteger(DataKeys.FEATURES_AUTO_TIME, 0);
		startingPos = data.getInteger(DataKeys.FEATURES_STARTING_POS, 0);
		blocker = data.getBoolean(DataKeys.FEATURES_BLOCKER, false);
		height = data.getInteger(DataKeys.FEATURES_HEIGHT, height);
	}

	@Override
	public boolean needsKeyboard() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void updateContents() {
		super.setTextContents(R.id.featureAutoNumberofFunctions,
				autoFunctions > 0 ? Integer.toString(autoFunctions) : "");
		super.setTextContents(R.id.featureAutoTime,
				autoTime > 0 ? Integer.toString(autoTime) : "");

		super.setState(R.id.featureBlockerNo, blocker == false);
		super.setState(R.id.featureBlockerYes, blocker == true);
		super.setTextContents(R.id.featureClimbTime,
				climbTime > 0 ? Integer.toString(climbTime) : "");

		super.setTextContents(R.id.featureHeight,
				height > 0 ? Integer.toString(height) : "");

		super.setState(R.id.featureLoaderBelt, loaderType == BELT);
		super.setState(R.id.featureLoaderScoop, loaderType == SCOOP);
		super.setState(R.id.featureLoaderFreq1, loaderType == 1);
		super.setState(R.id.featureLoaderFreq2, loaderType == 2);

		super.setState(R.id.featurePyramidTier1, pyramidTier == 1);
		super.setState(R.id.featurePyramidTier2, pyramidTier == 2);
		super.setState(R.id.featurePyramidTier3, pyramidTier == 3);

		super.setState(R.id.featureShooter90, shooterType == NINETY_DEGREE);
		super.setState(R.id.featureShooterLinear, shooterType == LINEAR);
		if (shooterType != NINETY_DEGREE && shooterType != LINEAR) {
			super.setState(R.id.featureShooterOther, true);
			try {
				super.setTextContents(R.id.featureShooterOtherInput,
						shooterTypeExt);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.setTextContents(R.id.featureShooterRange,
				shooterRange > 0 ? Integer.toString(shooterRange) : "");
		super.setTextContents(R.id.featureStartingPosition,
				startingPos > 0 ? Integer.toString(startingPos) : "");
	}

	@Override
	public void onTextChanged(CharSequence sq, int arg1, int arg2, int arg3) {
		String s = sq.toString();
		if (getTextContents(R.id.featureAutoNumberofFunctions)
				.equalsIgnoreCase(s)) {
			try {
				autoFunctions = Integer.valueOf(s);
			} catch (NumberFormatException ex) {
			}
		} else if (getTextContents(R.id.featureAutoTime).equalsIgnoreCase(s)) {
			try {
				autoTime = Integer.valueOf(s);
			} catch (NumberFormatException ex) {
			}
		} else if (getTextContents(R.id.featureClimbTime).equalsIgnoreCase(s)) {
			try {
				climbTime = Integer.valueOf(s);
			} catch (NumberFormatException ex) {
			}
		} else if (getTextContents(R.id.featureHeight).equalsIgnoreCase(s)) {
			try {
				height = Integer.valueOf(s);
			} catch (NumberFormatException ex) {
			}
		} else if (getTextContents(R.id.featureShooterOtherInput)
				.equalsIgnoreCase(s)) {
			shooterTypeExt = s;
		} else if (getTextContents(R.id.featureShooterRange)
				.equalsIgnoreCase(s)) {
			try {
				shooterRange = Integer.valueOf(s);
			} catch (NumberFormatException ex) {
			}
		} else if (getTextContents(R.id.featureStartingPosition)
				.equalsIgnoreCase(s)) {
			try {
				startingPos = Integer.valueOf(s);
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
		case R.id.featureBlockerYes:
			blocker = true;
			break;

		case R.id.featureBlockerNo:
			blocker = false;
			break;

		case R.id.featureLoaderFreq1:
			loaderFrequency = 1;
			break;

		case R.id.featureLoaderFreq2:
			loaderFrequency = 2;
			break;

		case R.id.featureShooter90:
			shooterType = NINETY_DEGREE;
			break;

		case R.id.featureShooterLinear:
			shooterType = LINEAR;
			break;

		case R.id.featureLoaderBelt:
			loaderType = BELT;
			break;

		case R.id.featureLoaderScoop:
			loaderType = SCOOP;
			break;

		case R.id.featurePyramidTier1:
			pyramidTier = 1;
			break;

		case R.id.featurePyramidTier2:
			pyramidTier = 2;
			break;

		case R.id.featurePyramidTier3:
			pyramidTier = 3;
			break;
		}
		super.postUpdate();
	}
}
