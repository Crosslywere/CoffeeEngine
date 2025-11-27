package com.crossly.coffee_engine.component.graphics;

import com.crossly.coffee_engine.component.SharedComponent;

import static org.lwjgl.opengl.GL46.*;

/**
 * @author Jude Ogboru
 */
public abstract class Texture extends SharedComponent {

	protected final int handle;

	protected Texture() {
		handle = glGenTextures();
	}

	public abstract void bind(int index);

	public int getTextureID() {
		return handle;
	}

	@Override
	public void cleanup() {
		glDeleteTextures(handle);
	}
}
