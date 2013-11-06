package com.skunk.scoutomatic.shocking.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ParentButton extends FieldButton {
	private List<FieldButton> children = new ArrayList<FieldButton>(0);

	public ParentButton(String name, FieldButton... children) {
		super(name);
		this.children.addAll(Arrays.asList(children));
	}

	public Iterator<FieldButton> getChildren() {
		return children.iterator();
	}

	@Override
	public boolean isVisible() {
		for (FieldButton bb : children) {
			if (bb.isVisible()) {
				return true;
			}
		}
		return false;
	}
}
