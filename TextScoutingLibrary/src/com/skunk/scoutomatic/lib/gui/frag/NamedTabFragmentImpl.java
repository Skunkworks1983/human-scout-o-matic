package com.skunk.scoutomatic.lib.gui.frag;

import android.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

/**
 * Created on: Sep 20, 2013
 * 
 * @author "Westin Miller"
 * 
 */
public abstract class NamedTabFragmentImpl extends Fragment implements
		INamedTabFragment {
	protected abstract void updateContents();

	public int getColor() {
		return 0x00000000;
	}

	public void postUpdate() {
		if (getView() != null) {
			getView().post(new Runnable() {
				public void run() {
					updateContents();
				}
			});
			getView().postInvalidate();
		}
	}

	protected final void setText(int id, String val) {
		View v = getView().findViewById(id);
		if (v != null && v instanceof TextView) {
			((TextView) v).setText(val);
		}
	}

	protected final String getTextContents(int id) {
		View v = getView().findViewById(id);
		if (v != null && v instanceof EditText) {
			return ((EditText) v).getText().toString();
		}
		return "";
	}

	protected final void setTextContents(int id, String s) {
		View v = getView().findViewById(id);
		if (v != null && v instanceof EditText) {
			((EditText) v).setText(s);
		}
	}

	protected final int getNumberValue(int id) {
		View v = getView().findViewById(id);
		if (v != null && v instanceof NumberPicker) {
			return ((NumberPicker) v).getValue();
		}
		return 0;
	}

	protected final void setNumberValue(int id, int value) {
		View v = getView().findViewById(id);
		if (v != null && v instanceof NumberPicker) {
			((NumberPicker) v).setValue(value);
		}
	}

	protected final void setNumberRange(View root, int id, int minimum,
			int maximum) {
		View v = root.findViewById(id);
		if (v != null && v instanceof NumberPicker) {
			((NumberPicker) v).setMinValue(minimum);
			((NumberPicker) v).setMaxValue(maximum);
			((NumberPicker) v).setWrapSelectorWheel(false);
			((NumberPicker) v)
					.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		}
	}

	protected final boolean getState(int id) {
		View vv = getView().findViewById(id);
		if (vv != null && vv instanceof CheckBox) {
			return ((CheckBox) vv).isChecked();
		}
		return false;
	}

	protected final void setState(int id, boolean state) {
		View vv = getView().findViewById(id);
		if (vv != null) {
			if (vv instanceof CompoundButton) {
				((CompoundButton) vv).setChecked(state);
			} else if (vv instanceof Checkable) {
				((Checkable) vv).setChecked(state);
			} else if (vv instanceof Button) {
				((Button) vv).setSelected(state);
			}
		}
	}

	protected final void setBackground(int id, int background) {
		View vv = getView().findViewById(id);
		if (vv != null) {
			vv.setBackgroundColor(background);
		}
	}
}
