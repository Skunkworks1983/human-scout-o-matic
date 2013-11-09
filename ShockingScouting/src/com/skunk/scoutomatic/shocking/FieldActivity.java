package com.skunk.scoutomatic.shocking;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.skunk.scoutomatic.shocking.comp.UltimateAscent;
import com.skunk.scoutomatic.shocking.control.ButtonManager;
import com.skunk.scoutomatic.shocking.event.CompositeTouchListener;
import com.skunk.scoutomatic.shocking.event.EventFactory;
import com.skunk.scoutomatic.shocking.event.SpecialEventListener;
import com.skunk.scoutomatic.shocking.event.SpecialTouchEvent;
import com.skunk.scoutomatic.shocking.field.FieldRenderer;
import com.skunk.scoutomatic.util.DatabaseInstance;

public class FieldActivity extends Activity {
	private FieldRenderer fieldRenderer;
	private GLSurfaceView glSurf;
	private ButtonManager buttonManager;
	private EventFactory eventFactory;

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

		buttonManager = new ButtonManager(this,
				(LinearLayout) findViewById(R.id.leftButtonContainer));

		eventFactory = new EventFactory(new SpecialEventListener() {
			@Override
			public void onSpecialTouch(SpecialTouchEvent e) {
				Log.d("EVT", e.toString());
				getButtonManager().onEvent(e);
			}
		});

		glSurf.setRenderer(fieldRenderer);
		glSurf.requestFocus();
		glSurf.setOnTouchListener(new CompositeTouchListener(fieldRenderer,
				eventFactory));

		new UltimateAscent(this).init();

	}

	public float getRobotX() {
		return fieldRenderer.getTouchX();
	}

	public float getRobotY() {
		return fieldRenderer.getTouchY();
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

	public EventFactory getEventFactory() {
		return eventFactory;
	}

	private DatabaseInstance db = new DatabaseInstance();

	public DatabaseInstance getMainDatabase() {
		return db;
	}

	public ButtonManager getButtonManager() {
		return buttonManager;
	}
}
