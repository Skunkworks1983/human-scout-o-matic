package com.skunk.scoutomatic.shocking.field;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;

import com.skunk.scoutomatic.shocking.FieldActivity;

public class FieldRenderer implements Renderer {
	private VertexObject c;
	private FieldActivity fieldActivity;

	public FieldRenderer(FieldActivity fieldActivity) {
		this.fieldActivity = fieldActivity;
		c = new VertexObject(
				new float[] { 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 1, 0 },
				new float[] { 1, 1, 1, 1, 1, 0, 0, 1, 0, 1, 0, 1, 0, 0, 1, 1 },
				null, new byte[] { 0, 1, 2, 2, 3, 0 });
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glClearColor(0f, 0f, 0f, 1f);
		gl.glDisable(GL10.GL_CULL_FACE);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);

		float ratio = (float) width / height;
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		// gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
		gl.glOrthof(0, width, height, 0, 0, 10);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glTranslatef(0, 0, -3.0f);
		gl.glScalef(500, 500, 500);

		c.draw(gl);
	}
}
