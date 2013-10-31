package com.skunk.scoutomatic.shocking;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.skunk.scoutomatic.shocking.field.FieldRenderer;

public class FieldActivity extends Activity {
	private FieldRenderer fieldRenderer;
	private GLSurfaceView glSurf;

	private LinearLayout buttonContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_field);

		if (fieldRenderer == null) {
			fieldRenderer = new FieldRenderer(this);
		}

		if (glSurf == null) {
			glSurf = new GLSurfaceView(this);
		}

		final FrameLayout contentView = (FrameLayout) findViewById(R.id.fullscreen_content);
		contentView.addView(glSurf);
		buttonContainer = (LinearLayout) findViewById(R.id.leftButtonContainer);
		glSurf.setRenderer(fieldRenderer);
		glSurf.setOnTouchListener(fieldRenderer);
		setButtonList(new String[] { "Lol1", "Lol2", "Lol3" }, null);
	}

	public void setButtonList(String[] names, final ButtonCallback callback) {
		buttonContainer.removeAllViews();
		for (final String name : names) {
			Button b = new Button(getApplicationContext());
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			b.setLayoutParams(params);
			b.setText(name);
			b.setTextSize(50);
			if (callback != null) {
				b.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View e) {
						callback.call(name, e);
					}
				});
			}
			buttonContainer.addView(b);
		}
		buttonContainer.requestLayout();
	}

	protected void onResume() {
		super.onResume();
		glSurf.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		glSurf.onPause();
	}
}
