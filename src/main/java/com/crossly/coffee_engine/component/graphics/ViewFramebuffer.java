package com.crossly.coffee_engine.component.graphics;

import static org.lwjgl.opengl.GL32.*;

/**
 * @author Jude Ogboru
 */
public class ViewFramebuffer extends Framebuffer {

	public ViewFramebuffer(int width, int height) {
		this(width, height, false);
	}

	public ViewFramebuffer(Number width, Number height) {
		this(width.intValue(), height.intValue());
	}

	protected ViewFramebuffer(int width, int height, boolean inherited) {
		super(width, height);
		var colorBuffer = new Texture2D(width, height);
		glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, colorBuffer.getTextureID(), 0);
		var depthBuffer = new Texture2D(width, height, GL_DEPTH_COMPONENT);
		glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, depthBuffer.getTextureID(), 0);
		addToRenderTextures(colorBuffer, depthBuffer);
		if (!inherited)
			validate();
	}

	@Override
	protected void resize() {
	}

	@Override
	protected void clearData() {
		Framebuffer.clear();
	}

	@Override
	protected void bindBuffers() {
	}
}
