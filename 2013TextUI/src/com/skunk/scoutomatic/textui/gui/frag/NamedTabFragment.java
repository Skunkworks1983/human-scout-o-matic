package com.skunk.scoutomatic.textui.gui.frag;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.skunk.scoutomatic.textui.DataCache;

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

	public abstract void storeInformation(DataCache data);

	public abstract void loadInformation(DataCache data);

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

	protected final void setState(int id, boolean state) {
		View vv = getView().findViewById(id);
		if (vv != null && vv instanceof CheckBox) {
			((CheckBox) vv).setChecked(state);
		}
	}
}
