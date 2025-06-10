package com.crossly.coffee_engine.component;

import com.crossly.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL46.*;

public final class Framebuffer extends Component {
    // -- Static Methods -- //
    public static void setClearColor(float red, float green, float blue) {
        glClearColor(red, green, blue, 1);
    }

    public static void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
    }

    public static void setDepthTest(boolean depthTest) {
        if (depthTest)
            glEnable(GL_DEPTH_TEST);
        else
            glDisable(GL_DEPTH_TEST);
    }

    // -- Framebuffer Object Stuff -- //
    private final List<Texture> renderTextures = new ArrayList<>();

    private final int handle;

    public Framebuffer(int width, int height) {
        handle = glGenFramebuffers();
        renderTextures.add(new Texture2D(width, height));
        bind();
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, renderTextures.getFirst().getTextureID(), 0);
        validate();
    }

    private Framebuffer(List<Pair<Texture, Integer>> texturesAndAttachments) {
        handle = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, handle);
        for (var textureAndAttachment : texturesAndAttachments) {
            var texture = textureAndAttachment.getFirst();
            var attachment = textureAndAttachment.getSecond();
            glFramebufferTexture(GL_FRAMEBUFFER, attachment, texture.getTextureID(), 0);
            renderTextures.add(texture);
        }
        validate();
    }

    private void validate() {
        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
            throw new RuntimeException("Framebuffer is incomplete!");
        bind();
    }

    public void bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, handle);
    }

    public static void unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public int getTextureCount() {
        return renderTextures.size();
    }

    public void bindAllTextures() {
        for (int i = 0; i < renderTextures.size(); i++)
            renderTextures.get(i).bind(i);
    }

    public Texture getTexture(int index) {
        return renderTextures.get(index);
    }

    @Override
    protected void cleanup() {
        glDeleteFramebuffers(handle);
        renderTextures.forEach(Texture::cleanup);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private enum TextureTypes {
            RGBA_COLOR_ATTACHMENT_2D,
            RGBA_COLOR_ATTACHMENT_3D,
            INT_COLOR_ATTACHMENT_2D,
            INT_COLOR_ATTACHMENT_3D,
            DEPTH_ATTACHMENT_2D,
            DEPTH_ATTACHMENT_3D
        }
        private final List<TextureTypes> attachments = new ArrayList<>();

        public Builder add2DColor() {
            attachments.add(TextureTypes.RGBA_COLOR_ATTACHMENT_2D);
            return this;
        }

        public Builder add2DInteger() {
            attachments.add(TextureTypes.INT_COLOR_ATTACHMENT_2D);
            return this;
        }

        public Builder add2DDepth() {
            attachments.add(TextureTypes.DEPTH_ATTACHMENT_2D);
            return this;
        }

        public Framebuffer build(int width, int height) {
            var texturesAndAttachments = new ArrayList<Pair<Texture, Integer>>();
            int colorAttachment = 0;
            for (var attachment : attachments) {
                switch (attachment) {
                    case RGBA_COLOR_ATTACHMENT_2D ->
                            texturesAndAttachments.add(new Pair<>(new Texture2D(width, height), GL_COLOR_ATTACHMENT0 + colorAttachment++));
//                    case RGBA_COLOR_ATTACHMENT_3D ->
//                            texturesAndAttachments.add(new Pair<>(new Texture3D(width, height), GL_COLOR_ATTACHMENT0 + colorAttachment));
                    case INT_COLOR_ATTACHMENT_2D ->
                            texturesAndAttachments.add(new Pair<>(new Texture2D(width, height, GL_R32I, GL_RED_INTEGER, GL_INT), GL_COLOR_ATTACHMENT0 + colorAttachment++));

                    case DEPTH_ATTACHMENT_2D ->
                            texturesAndAttachments.add(new Pair<>(new Texture2D(width, height, GL_DEPTH_COMPONENT), GL_DEPTH_ATTACHMENT));

                }
            }
            return new Framebuffer(texturesAndAttachments);
        }
    }
}
