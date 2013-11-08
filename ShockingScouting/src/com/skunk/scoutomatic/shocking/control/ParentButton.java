package com.skunk.scoutomatic.shocking.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.skunk.scoutomatic.shocking.FieldActivity;

public class ParentButton extends FieldButton {
	private List<FieldButton> children = new ArrayList<FieldButton>(0);

	public ParentButton(String name, FieldButton... children) {
		super(name);
		this.children.addAll(Arrays.asList(children));
	}

	public Iterable<FieldButton> getChildren() {
		return children;
	}

	@Override
	public boolean isVisible(FieldActivity act) {
		for (FieldButton bb : children) {
			if (bb.isVisible(act)) {
				return true;
			}
		}
		return false;
	}
}
