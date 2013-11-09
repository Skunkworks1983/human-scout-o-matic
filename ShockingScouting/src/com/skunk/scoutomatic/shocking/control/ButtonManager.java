package com.skunk.scoutomatic.shocking.control;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

import com.skunk.scoutomatic.shocking.FieldActivity;
import com.skunk.scoutomatic.shocking.event.SpecialDragEvent;
import com.skunk.scoutomatic.shocking.event.SpecialTouchEvent;
import com.skunk.scoutomatic.util.DatabaseInstance;
import com.skunk.scoutomatic.util.ObjectFilter;

public class ButtonManager {
	private LinearLayout buttonContainer;
	private ParentButton currentRoot;
	private FieldActivity frontend;
	private Map<ObjectFilter<SpecialTouchEvent>, ParentButton> triggers = new HashMap<ObjectFilter<SpecialTouchEvent>, ParentButton>();
	private DatabaseInstance cacheInstance;

	private DummyButton confirmButton = new DummyButton("Confirm");
	private DummyButton cancelButton = new DummyButton("Cancel");

	public ButtonManager(FieldActivity backend, LinearLayout ctnr) {
		this.frontend = backend;
		this.buttonContainer = ctnr;
	}

	public void registerButtonTrigger(ObjectFilter<SpecialTouchEvent> trigger,
			ParentButton button) {
		triggers.put(trigger, button);
	}

	public void reset() {
		buttonContainer.removeAllViews();
		triggers.clear();
	}

	private void updateButtonList() {
		int childCount = 0;
		try {
			if (currentRoot != null) {
				Iterator<FieldButton> children = currentRoot.getChildren()
						.iterator();
				FieldButton nextFake = currentRoot.isCaching() ? confirmButton
						: cancelButton;
				float size = Math.min(50, 35 * 7 / (currentRoot.getChildren()
						.size() + (currentRoot.isCaching() ? 2 : 1)));
				while (children.hasNext() || nextFake != null) {
					final FieldButton button = children.hasNext() ? children
							.next() : nextFake;
					if (button == confirmButton) {
						nextFake = cancelButton;
					} else if (button == cancelButton) {
						nextFake = null;
					}

					if (button.isVisible(frontend)) {
						Button b = null;
						if (childCount < buttonContainer.getChildCount()) {
							b = (Button) buttonContainer.getChildAt(childCount);
							childCount++;
						} else {
							b = new Button(frontend.getApplicationContext());
							buttonContainer.addView(b);
							childCount++;
						}
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT);
						b.setLayoutParams(params);
						b.setText(button.getName());
						b.setTextSize(size);
						b.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View e) {
								ButtonManager.this.onClick(button, e);
							}
						});
						b.invalidate();
					}
				}
			}
		} catch (Exception e) {
		}
		if (childCount < buttonContainer.getChildCount()) {
			buttonContainer.removeViews(childCount,
					buttonContainer.getChildCount() - childCount);
		}
		buttonContainer.requestLayout();
		buttonContainer.invalidate();
	}

	protected void onClick(FieldButton button, View e) {
		if (button == cancelButton) {
			setRoot(null, false);
		} else if (button == confirmButton) {
			setRoot(null, true);
		} else if (button instanceof ParentButton) {
			setRoot(currentRoot, false);
		} else if (button instanceof ActionButton) {
			((ActionButton) button)
					.run(frontend,
							cacheInstance != null && currentRoot.isCaching() ? cacheInstance
									: frontend.getMainDatabase());
			if (currentRoot.isOneShot()) {
				setRoot(null, true);
			}
		}
	}

	protected void setRoot(ParentButton root, boolean save) {
		if (currentRoot == root) {
			return;
		}
		if (cacheInstance != null && save) {
			frontend.getMainDatabase().merge(cacheInstance);
		}
		currentRoot = root;
		if (currentRoot != null && currentRoot.isCaching()) {
			cacheInstance = new DatabaseInstance();
		} else {
			cacheInstance = null;
		}
		updateButtonList();
	}

	public void onEvent(SpecialTouchEvent evt) {
		// Check the triggers; if triggered set it nicely
		for (Entry<ObjectFilter<SpecialTouchEvent>, ParentButton> trigger : triggers
				.entrySet()) {
			if (trigger.getKey().accept(evt)) {
				currentRoot = trigger.getValue();
				update();
				break;
			}
		}
	}

	public void update() {
		updateButtonList();
	}

	public static final ObjectFilter<SpecialTouchEvent> HELD_DOWN_1_EVENT = new ObjectFilter<SpecialTouchEvent>() {
		@Override
		public boolean accept(SpecialTouchEvent t) {
			if (t instanceof SpecialDragEvent) {
				return ((SpecialDragEvent) t).getDistance() < 20
						&& ((SpecialDragEvent) t).getTime() > 500
						&& ((SpecialDragEvent) t).getTouchCount() == 1;
			}
			return false;
		}
	};
	public static final ObjectFilter<SpecialTouchEvent> HELD_DOWN_MULTI_EVENT = new ObjectFilter<SpecialTouchEvent>() {
		@Override
		public boolean accept(SpecialTouchEvent t) {
			if (t instanceof SpecialDragEvent) {
				return ((SpecialDragEvent) t).getDistance() < 20
						&& ((SpecialDragEvent) t).getTime() > 500
						&& ((SpecialDragEvent) t).getTouchCount() == 2;
			}
			return false;
		}
	};
}
