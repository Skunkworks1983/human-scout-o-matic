package com.skunk.scoutomatic.shocking.field;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class VertexObject {
	private FloatBuffer mVertexBuffer;
	private FloatBuffer mColorBuffer;
	private FloatBuffer mTextureBuffer;
	private ByteBuffer mIndexBuffer;

	public VertexObject(float vertices[], float colors[], float[] texture,
			byte[] indices) {
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		mVertexBuffer = vbb.asFloatBuffer();
		mVertexBuffer.put(vertices);
		mVertexBuffer.position(0);

		if (colors != null) {
			ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
			cbb.order(ByteOrder.nativeOrder());
			mColorBuffer = cbb.asFloatBuffer();
			mColorBuffer.put(colors);
			mColorBuffer.position(0);
		}

		if (texture != null) {
			ByteBuffer cbb = ByteBuffer.allocateDirect(texture.length * 4);
			cbb.order(ByteOrder.nativeOrder());
			mTextureBuffer = cbb.asFloatBuffer();
			mTextureBuffer.put(texture);
			mTextureBuffer.position(0);
		}

		mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
		mIndexBuffer.put(indices);
		mIndexBuffer.position(0);
	}

	public void draw(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		if (mColorBuffer != null) {
			gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		}
		if (mColorBuffer != null) {
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		}

		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);

		if (mColorBuffer != null) {
			gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);
		}
		if (mTextureBuffer != null) {
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);
		}
		gl.glDrawElements(GL10.GL_TRIANGLES,
				((int) mIndexBuffer.limit() / 3) * 3, GL10.GL_UNSIGNED_BYTE,
				mIndexBuffer);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

		if (mColorBuffer != null) {
			gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		}
		if (mColorBuffer != null) {
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		}
	}
}