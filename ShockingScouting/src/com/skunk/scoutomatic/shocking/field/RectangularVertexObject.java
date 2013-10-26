package com.skunk.scoutomatic.shocking.field;

import javax.microedition.khronos.opengles.GL10;

public class RectangularVertexObject extends VertexObject {
	public RectangularVertexObject(float x1, float y1, float x2, float y2) {
		super(GL10.GL_TRIANGLE_STRIP, new float[] { x1, y1, 0, x2, y1, 0, x2,
				y2, 0, x1, y2, 0 }, new float[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1 }, new float[] { 0f, 0f, 1f, 0f, 1f, 1f, 0f,
				1f }, new byte[] { 1, 2, 0, 3 });
	}

	public void setLocation(float x1, float y1, float x2, float y2) {
		super.mVertexBuffer.position(0);
		super.mVertexBuffer.put(new float[] { x1, y1, 0, x2, y1, 0, x2, y2, 0,
				x1, y2, 0 });
		super.mVertexBuffer.position(0);
	}

	public void setColor(float r, float g, float b, float a) {
		super.mColorBuffer.position(0);
		super.mColorBuffer.put(new float[] { a, r, g, b, a, r, g, b, a, r, g,
				b, a, r, g, b });
		super.mColorBuffer.position(0);
	}
}
