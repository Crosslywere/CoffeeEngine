package com.crossly.coffee_engine.component.graphics;

import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.stb.STBImage.*;

/**
 * @author Jude Ogboru
 */
public final class Texture2D extends Texture {

	private final int width, height;
	private int textureFormat;

	public Texture2D(String imagePath) {
		this(imagePath, false);
	}

	public Texture2D(String imagePath, boolean flipVertical) {
		textureFormat = GL_RGBA;
		glBindTexture(GL_TEXTURE_2D, handle);
		int[] w = new int[1], h = new int[1], ch = new int[1];
		stbi_set_flip_vertically_on_load(flipVertical);
		var buffer = stbi_load(imagePath, w, h, ch, 0);
		if (buffer == null)
			throw new RuntimeException(stbi_failure_reason());
		width = w[0];
		height = h[0];
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		glGenerateMipmap(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, 0);
		stbi_image_free(buffer);
	}

	public Texture2D(int width, int height) {
		this(width, height, GL_RGBA);
	}

	public Texture2D(int width, int height, int formats) {
		this(width, height, formats, formats, GL_UNSIGNED_BYTE);
	}

	public Texture2D(int width, int height, int internalFormat, int format, int dataType) {
		this.width = width;
		this.height = height;
		this.textureFormat = format;
		glBindTexture(GL_TEXTURE_2D, handle);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, dataType, 0);
		glGenerateMipmap(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getTextureFormat() {
		return textureFormat;
	}

	@Override
	public void bind(int index) {
		glActiveTexture(GL_TEXTURE0 + index);
		glBindTexture(GL_TEXTURE_2D, handle);
	}

	public static Texture2D createColorRenderTexture(int width, int height) {
		return new Texture2D(width, height);
	}

	public static Texture2D createDepthRenderTexture(int width, int height) {
		return new Texture2D(width, height, GL_DEPTH_COMPONENT, GL_DEPTH_COMPONENT, GL_FLOAT);
	}

	public static Texture2D createDepthStencilRenderTexture(int width, int height) {
		throw new UnsupportedOperationException("'createDepthStencilRenderTexture' has not been implemented!");
	}
}
