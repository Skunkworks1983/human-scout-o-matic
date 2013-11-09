package com.skunk.scoutomatic.shocking.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.skunk.scoutomatic.shocking.FieldActivity;

public class ParentButton extends FieldButton {
	public static final int CACHING = 2;
	public static final int SINGLE_SHOT = 1;

	private List<FieldButton> children = new ArrayList<FieldButton>(0);
	private byte flags = 0;

	public ParentButton(String name, int flags, FieldButton... children) {
		super(name);
		this.flags = (byte) flags;
		this.children.addAll(Arrays.asList(children));
	}

	public ParentButton(String name, FieldButton... children) {
		this(name, 0, children);
	}

	public Collection<FieldButton> getChildren() {
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

	public boolean isCaching() {
		return (flags & CACHING) == CACHING;
	}

	public boolean isOneShot() {
		return (flags & SINGLE_SHOT) == SINGLE_SHOT;
	}
}
