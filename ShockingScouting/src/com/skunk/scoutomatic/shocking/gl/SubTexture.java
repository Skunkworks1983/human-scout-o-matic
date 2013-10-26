package com.skunk.scoutomatic.shocking.gl;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;

public class SubTexture {
	private Texture reference;
	public final float minU, minV, maxU, maxV;

	public SubTexture(Texture base, float minU, float minV, float maxU,
			float maxV) {
		this.reference = base;
		this.minU = minU;
		this.maxU = maxU;
		this.minV = minV;
		this.maxV = maxV;
	}

	public final Texture getReference() {
		return reference;
	}

	public float getWidth() {
		return reference.getWidth() * (maxU - minU);
	}

	public float getHeight() {
		return reference.getHeight() * (maxV - minV);
	}

	public void bind(GL10 gl) {
		reference.bind(gl);
	}

	public Bitmap getBitmap() {
		return reference.getBitmap();
	}

	public void loadToGPU(GL10 gl) {
		reference.loadToGPU(gl);
	}
}
