package com.skunk.scoutomatic.shocking.field;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.skunk.scoutomatic.shocking.FieldActivity;
import com.skunk.scoutomatic.shocking.R;
import com.skunk.scoutomatic.shocking.gl.Texture;

public class FieldRenderer implements Renderer, OnTouchListener {
	private static final float ROBOT_SIZE = 50f;

	private FieldActivity fieldActivity;

	private RectangularVertexObject fieldObject;
	private Texture fieldTexture;

	private RectangularVertexObject robotObject;
	private Texture robotTexture;

	private PathVertexObject pathObject;

	private float worldX = 0.5f, worldY = 0.5f;

	public FieldRenderer(FieldActivity fieldActivity) {
		this.fieldActivity = fieldActivity;

		this.fieldTexture = new Texture(fieldActivity.getApplicationContext(),
				R.drawable.field);
		this.fieldTexture.getBitmap();
		fieldObject = new RectangularVertexObject(0, 0, 1, 1);

		this.robotTexture = new Texture(fieldActivity.getApplicationContext(),
				R.drawable.first_robot);
		this.robotTexture.getBitmap();
		robotObject = new RectangularVertexObject(0, 0, ROBOT_SIZE, ROBOT_SIZE);

		pathObject = new PathVertexObject(worldX, worldY, 0, 10, 0xffff0000,
				0x990000ff);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glClearColor(0f, 0f, 0f, 1f);
		gl.glFrontFace(GL10.GL_CW);
		gl.glDisable(GL10.GL_CULL_FACE);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_DST_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		gl.glEnable(GL10.GL_TEXTURE_2D);
		fieldTexture.loadToGPU(gl);
		robotTexture.loadToGPU(gl);

		gl.glDisable(GL10.GL_DITHER);
		gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
				GL10.GL_MODULATE);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0, width, height, 0, 0, 10);

		// Update field object
		{
			float ySize = width / fieldTexture.getWidth()
					* fieldTexture.getHeight();
			float xSize = height / fieldTexture.getHeight()
					* fieldTexture.getWidth();
			if (ySize < height) {
				// Fill X
				float yOff = (height / 2) - (ySize / 2);
				fieldObject.setLocation(0, yOff, width, yOff + ySize);
			} else {
				// Fill Y
				float xOff = (width / 2) - (xSize / 2);
				fieldObject.setLocation(xOff, 0, xOff + xSize, height);
			}
		}
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glEnable(GL10.GL_TEXTURE_2D);

		fieldTexture.bind(gl);
		fieldObject.draw(gl);

		robotTexture.bind(gl);
		gl.glPushMatrix();
		gl.glTranslatef(worldX - (ROBOT_SIZE / 2), worldY - (ROBOT_SIZE / 2), 0);
		robotObject.draw(gl);
		gl.glPopMatrix();

		pathObject.pushVertex(worldX, worldY, 0);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		pathObject.draw(gl);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			worldX = event.getX();
			worldY = event.getY();
			break;
		}
		return true;
	}
}
