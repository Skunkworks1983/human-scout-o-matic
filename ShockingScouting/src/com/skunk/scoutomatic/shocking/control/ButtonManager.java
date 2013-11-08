package com.skunk.scoutomatic.shocking.control;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

import com.skunk.scoutomatic.shocking.FieldActivity;
import com.skunk.scoutomatic.shocking.event.SpecialTouchEvent;
import com.skunk.scoutomatic.util.ObjectFilter;

public class ButtonManager {
	private LinearLayout buttonContainer;
	private ParentButton currentRoot;
	private FieldActivity frontend;
	private Map<ObjectFilter<SpecialTouchEvent>, ParentButton> triggers = new HashMap<ObjectFilter<SpecialTouchEvent>, ParentButton>();

	public ButtonManager(FieldActivity backend, LinearLayout ctnr) {
		this.frontend = backend;
		this.buttonContainer = ctnr;
	}

	public void registerButtonTrigger(ObjectFilter<SpecialTouchEvent> trigger,
			ParentButton button) {
		triggers.put(trigger, button);
	}

	private void updateButtonList() {
		int childCount = 0;
		try {
			for (final FieldButton button : currentRoot.getChildren()) {
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
					b.setTextSize(50);
					b.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View e) {
							ButtonManager.this.onClick(button, e);
						}
					});
					b.invalidate();
				}
			}
		} catch (Exception e) {
			Log.e("ERROR", e.toString());
		}
		Log.d("CHILDREN", childCount + " of " + buttonContainer.getChildCount());
		if (childCount < buttonContainer.getChildCount()) {
			buttonContainer.removeViews(childCount,
					buttonContainer.getChildCount() - childCount);
		}
		buttonContainer.requestLayout();
		buttonContainer.invalidate();
	}

	protected void onClick(FieldButton button, View e) {
		if (button instanceof ParentButton) {
			currentRoot = (ParentButton) button;
			updateButtonList();
		} else if (button instanceof ActionButton) {
			((ActionButton) button).run(frontend);
		}
	}

	public void onEvent(SpecialTouchEvent evt) {
		// Check the triggers; if triggered set it nicely
		for (Entry<ObjectFilter<SpecialTouchEvent>, ParentButton> trigger : triggers
				.entrySet()) {
			Log.d("EVT", evt.toString());
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
}
