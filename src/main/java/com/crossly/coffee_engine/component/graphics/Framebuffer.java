package com.crossly.coffee_engine.component.graphics;

import com.crossly.coffee_engine.component.OwnedComponent;

import java.util.List;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL30.*;

/**
 * @author Jude Ogboru
 */
public abstract class Framebuffer extends OwnedComponent {

	public static void setClearColor(float r, float g, float b) {
		glClearColor(r, g, b, 1f);
	}

	public static void clear() {
		glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
	}

	public static void setDepthTestEnabled(boolean depthTestEnabled) {
		if (depthTestEnabled)
			glEnable(GL_DEPTH_TEST);
		else
			glDisable(GL_DEPTH_TEST);
	}

	public static void unbind() {
		glBindFramebuffer(GL_READ_FRAMEBUFFER, 0);
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	private int width, height;
	protected int handle;

	protected List<Texture> renderTextures;

	protected Framebuffer(int width, int height) {
		this.width = width;
		this.height = height;
		handle = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, handle);
	}

	public final void setDimension(int width, int height) {
		this.width = width;
		this.height = height;
		resize();
	}

	public final int getWidth() {
		return width;
	}

	public final int getHeight() {
		return height;
	}

	public final Texture getRenderTexture(int index) {
		if (index >= renderTextures.size())
			throw new IndexOutOfBoundsException(
					"Index out of bounds when trying to get render texture from frame buffer!");

		return renderTextures.get(index);
	}

	protected final void addToRenderTextures(Texture texture) {
		if (renderTextures.isEmpty() || !renderTextures.contains(texture))
			renderTextures.add(texture);
	}

	protected final void addToRenderTextures(Texture... textures) {
		for (var texture : textures)
			addToRenderTextures(texture);
	}

	protected final void validate() {
		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
			throw new RuntimeException("Framebuffer is incomplete!");
		bind();
	}

	public final void bind() {
		glBindFramebuffer(GL_FRAMEBUFFER, handle);
		glViewport(0, 0, width, height);
	}

	protected abstract void resize();

	@Override
	protected void cleanup() {
		glDeleteFramebuffers(handle);
		renderTextures.forEach(Texture::cleanup);
	}

}
