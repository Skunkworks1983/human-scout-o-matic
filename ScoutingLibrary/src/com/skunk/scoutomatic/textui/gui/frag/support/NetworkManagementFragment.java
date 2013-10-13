package com.skunk.scoutomatic.textui.gui.frag.support;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pi.scoutomatic.lib.data.DataCache;
import com.pi.scoutomatic.lib.net.BackendInterface;
import com.pi.scoutomatic.lib.net.NetworkAction;
import com.skunk.scoutomatic.lib.R;
import com.skunk.scoutomatic.textui.gui.ScoutingActivity;
import com.skunk.scoutomatic.textui.gui.frag.INamedTabFragment;
import com.skunk.scoutomatic.textui.gui.frag.NamedTabFragmentImpl;
import com.skunk.scoutomatic.textui.gui.frag.dummy.GoBackFragment;

/**
 * Created on: Oct 11, 2013
 * 
 * @author "Westin Miller"
 * 
 */
public class NetworkManagementFragment extends NamedTabFragmentImpl {
	@Override
	public String getName() {
		return "Network";
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.network_view, container, false);
		View list = v.findViewById(R.id.networkBacklogList);
		if (list != null && list instanceof ListView) {
			((ListView) list).setAdapter(new NetworkListAdapter());
		}
		return v;
	}

	@Override
	public Class<? extends INamedTabFragment> getNext() {
		return null;
	}

	@Override
	public Class<? extends INamedTabFragment> getPrevious() {
		return GoBackFragment.class;
	}

	@Override
	public void storeInformation(DataCache data) {
	}

	@Override
	public void loadInformation(DataCache data) {
	}

	@Override
	public boolean needsKeyboard() {
		return false;
	}

	@Override
	protected void updateContents() {
		if (getView() == null) {
			return;
		}
	}

	private class NetworkListAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			Activity act = getActivity();
			if (act != null && act instanceof ScoutingActivity) {
				BackendInterface backend = ((ScoutingActivity) act)
						.getBackend();
				return backend.getBacklog().size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View lstItem = convertView;
			if (lstItem == null || !(convertView instanceof TextView)) {
				lstItem = new TextView(parent.getContext());
			}
			String value = "";
			Activity act = getActivity();
			if (act != null && act instanceof ScoutingActivity) {
				BackendInterface backend = ((ScoutingActivity) act)
						.getBackend();
				if (backend.getBacklog().size() > position && position >= 0) {
					NetworkAction nact = backend.getBacklog().get(position);
					if (nact != null) {
						value = nact.getID() + " tries: " + nact.getTries()
								+ "\n" + nact.getError();
					}
				}
			}
			((TextView) lstItem).setText(value);
			return lstItem;
		}
	}
}
