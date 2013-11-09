package com.skunk.scoutomatic.shocking.field;

import javax.microedition.khronos.opengles.GL10;

import com.skunk.scoutomatic.shocking.gl.SubTexture;

public class RectangularVertexObject extends VertexObject {
	public RectangularVertexObject(float x1, float y1, float x2, float y2) {
		super(GL10.GL_TRIANGLE_STRIP, new float[] { x1, y1, 0, x2, y1, 0, x2,
				y2, 0, x1, y2, 0 }, new float[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1 }, new float[] { 0f, 0f, 1f, 0f, 1f, 1f, 0f,
				1f }, new byte[] { 1, 2, 0, 3 });
	}

	public RectangularVertexObject setLocation(float x1, float y1, float x2,
			float y2) {
		super.mVertexBuffer.position(0);
		super.mVertexBuffer.put(new float[] { x1, y1, 0, x2, y1, 0, x2, y2, 0,
				x1, y2, 0 });
		super.mVertexBuffer.position(0);
		return this;
	}

	public RectangularVertexObject setColor(float r, float g, float b, float a) {
		super.mColorBuffer.position(0);
		super.mColorBuffer.put(new float[] { a, r, g, b, a, r, g, b, a, r, g,
				b, a, r, g, b });
		super.mColorBuffer.position(0);
		return this;
	}

	public RectangularVertexObject setTextureClip(float minU, float minV,
			float maxU, float maxV) {
		super.mTextureBuffer.position(0);
		super.mTextureBuffer.put(new float[] { minU, minV, maxU, minV, maxU,
				maxV, minU, maxV });
		super.mTextureBuffer.position(0);
		return this;
	}

	public RectangularVertexObject setTextureClip(SubTexture fieldTexture) {
		return setTextureClip(fieldTexture.minU, fieldTexture.minV,
				fieldTexture.maxU, fieldTexture.maxV);
	}

	public boolean contains(float x, float y) {
		return x >= mVertexBuffer.get(0) && y >= mVertexBuffer.get(1)
				&& x <= mVertexBuffer.get(6) && y <= mVertexBuffer.get(7);
	}

	public float getWidth() {
		return mVertexBuffer.get(6) - mVertexBuffer.get(0);
	}

	public float getHeight() {
		return mVertexBuffer.get(7) - mVertexBuffer.get(1);
	}

	public float clampX(float x) {
		return Math
				.min(Math.max(x, mVertexBuffer.get(0)), mVertexBuffer.get(6))
				/ getWidth();
	}

	public float clampY(float y) {
		return Math
				.min(Math.max(y, mVertexBuffer.get(1)), mVertexBuffer.get(7))
				/ getHeight();
	}
}
