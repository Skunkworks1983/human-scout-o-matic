package com.skunk.scoutomatic.shocking.field;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Color;

public class PathVertexObject extends VertexObject {
	private static float[] generateVerticies(int count, float x, float y,
			float z) {
		float[] buff = new float[3 * count];
		for (int i = 0; i < buff.length; i += 3) {
			buff[i] = x;
			buff[i + 1] = y;
			buff[i + 2] = z;
		}
		return buff;
	}

	private static byte[] generateIndexBuffer(int count) {
		byte[] buff = new byte[count];
		for (byte b = 0; b < count; b++) {
			buff[b] = b;
		}
		return buff;
	}

	private static float[] generateColorBuffer(int count, int from, int to) {
		float[] colors = new float[count * 4];
		for (int i = 0; i < colors.length; i += 4) {
			float blend = ((float) i) / ((float) colors.length);
			colors[i] = (Color.red(to) * blend / 255f)
					+ (Color.red(from) * (1f - blend) / 255f);
			colors[i + 1] = (Color.green(to) * blend / 255f)
					+ (Color.green(from) * (1f - blend) / 255f);
			colors[i + 2] = (Color.blue(to) * blend / 255f)
					+ (Color.blue(from) * (1f - blend) / 255f);
			colors[i + 3] = (Color.alpha(to) * blend / 255f)
					+ (Color.alpha(from) * (1f - blend) / 255f);
		}
		return colors;
	}

	public PathVertexObject(float x, float y, float z, int count, int from,
			int to) {
		super(GL10.GL_LINE_STRIP, generateVerticies(count, x, y, z),
				generateColorBuffer(count, from, to), null,
				generateIndexBuffer(count));
	}

	public void pushVertex(float x, float y, float z) {
		// Cycle other verticies down
		float[] buff = new float[mVertexBuffer.limit() - 3];
		mVertexBuffer.get(buff);
		mVertexBuffer.rewind();
		mVertexBuffer.put(x);
		mVertexBuffer.put(y);
		mVertexBuffer.put(z);
		mVertexBuffer.put(buff);
		mVertexBuffer.flip();
	}
}
