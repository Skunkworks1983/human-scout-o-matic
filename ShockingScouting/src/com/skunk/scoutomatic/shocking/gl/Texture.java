package com.skunk.scoutomatic.shocking.gl;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

public class Texture {
	private static int boundTexture = -1;

	private int textureID = -1;
	private Context ctx;
	private int resourceID;
	private Bitmap bitmap;

	public Texture(Context ctx, int resourceID) {
		this.resourceID = resourceID;
		this.ctx = ctx;
	}

	public Bitmap getBitmap() {
		if (bitmap == null || bitmap.isRecycled()) {
			bitmap = BitmapFactory.decodeResource(ctx.getResources(),
					resourceID);
			Log.d("GLES", "Loaded " + resourceID + " to RAM.");
		}
		return bitmap;
	}

	public void disposeBitmap() {
		bitmap.recycle();
	}

	public void loadToGPU(GL10 gl) {
		if (textureID != -1) {
			return;
		}
		getBitmap();
		int[] textures = new int[1];
		gl.glGenTextures(1, textures, 0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		textureID = textures[0];
		Log.d("GLES", "Loaded " + resourceID + " to GPU as " + textureID);
		

		// Auto recycle
		bitmap.recycle();
	}

	public void unloadFromGPU(GL10 gl) {
		if (textureID != -1) {
			gl.glDeleteTextures(1, new int[] { textureID }, 0);
		}
		textureID = -1;
	}

	public void bind(GL10 gl) {
		loadToGPU(gl);
		if (boundTexture != textureID) {
			boundTexture = textureID;
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureID);
		}
	}
}
