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
	private static final float ROBOT_SIZE = 0.05f;

	private FieldActivity fieldActivity;

	private VertexObject fieldObject;
	private Texture fieldTexture;

	private VertexObject robotObject;
	private Texture robotTexture;

	private PathVertexObject pathObject;

	private float worldsPerPixel = 1;
	private float worldX = 0.5f, worldY = 0.5f;

	public FieldRenderer(FieldActivity fieldActivity) {
		this.fieldActivity = fieldActivity;

		this.fieldTexture = new Texture(fieldActivity.getApplicationContext(),
				R.drawable.field);
		this.fieldTexture.getBitmap();
		fieldObject = new VertexObject(GL10.GL_TRIANGLES, new float[] { 0, 0,
				0, 1, 0, 0, 1, 1, 0, 0, 1, 0 }, new float[] { 1, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, new float[] { 1, 0, 1, 1, 0, 1,
				0, 0 }, new byte[] { 0, 1, 2, 3, 2, 0 });

		this.robotTexture = new Texture(fieldActivity.getApplicationContext(),
				R.drawable.first_robot);
		this.robotTexture.getBitmap();
		robotObject = new VertexObject(GL10.GL_TRIANGLES, new float[] { 0, 0,
				0, ROBOT_SIZE, 0, 0, ROBOT_SIZE, ROBOT_SIZE, 0, 0, ROBOT_SIZE,
				0 }, new float[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
				1 }, new float[] { 1, 0, 1, 1, 0, 1, 0, 0 }, new byte[] { 0, 1,
				2, 3, 2, 0 });

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

		fieldTexture.loadToGPU(gl);
		robotTexture.loadToGPU(gl);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		worldsPerPixel = (1f / (float) width);
		gl.glOrthof(0, 1f, (height / (float) width), 0, 0, 10);
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
			worldX = event.getX() * worldsPerPixel;
			worldY = event.getY() * worldsPerPixel;
			break;
		}
		return true;
	}
}