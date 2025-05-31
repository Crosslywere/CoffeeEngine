package com.crossly.components;

import com.crossly.components.subcomponents.Mesh;
import com.crossly.components.subcomponents.Renderbuffer;
import com.crossly.interfaces.Component;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL46.*;

/**
 * @author Jude Ogboru
 */
public class Framebuffer implements Component {

    public static void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
    }

    public static void setClearColor(float r, float g, float b) {
        glClearColor(r, g, b, 1);
    }

    public static void setClearColor(Vector3f color) {
        setClearColor(color.x(), color.y(), color.z());
    }

    public static void enableDepthTest() {
        glEnable(GL_DEPTH_TEST);
    }

    public static void cullBackFaces() {
        glEnable(GL_CULL_FACE);
    }

    public static void drawPoints() {
        glPolygonMode(GL_FRONT_AND_BACK, GL_POINT);
    }

    public static void drawLines() {
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
    }

    public static void drawFaces() {
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
    }

    protected final int framebufferId, width, height;
    protected FramebufferTexture2D texture;
    protected final Renderbuffer renderbuffer;
    protected int referenceCount = 0;

    public Framebuffer(int width, int height) {
        this.width = width;
        this.height = height;
        framebufferId = glGenFramebuffers();
        bind();
        // Color Attachment 0
        texture = new FramebufferTexture2D(width, height, GL_COLOR_ATTACHMENT0);
        texture.incrementReference();
        glFramebufferTexture2D(GL_FRAMEBUFFER, texture.getUsage(), GL_TEXTURE_2D, texture.getTextureId(), 0);
        // Depth stencil Renderbuffer
        renderbuffer = new Renderbuffer(width, height, GL_DEPTH24_STENCIL8, GL_DEPTH_STENCIL_ATTACHMENT);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, renderbuffer.getUsage(), GL_RENDERBUFFER, renderbuffer.getRenderbufferId());
        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            throw new RuntimeException("Framebuffer incomplete!");
        }
        unbind();
    }

    @Override
    public void cleanup() {
        if (referenceCount == 0) {
            texture.cleanup();
            renderbuffer.cleanup();
            glDeleteFramebuffers(framebufferId);
        } else
            decrementReference();
    }

    protected void bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, framebufferId);
    }

    public static void unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    @Override
    public void use() {
        bind();
        glViewport(0, 0, width, height);
    }

    @Override
    public void render() {
        render(ShaderProgram.getDefault2dTexturingShader(), "uTexture");
    }

    @Override
    public void incrementReference() {
        referenceCount++;
    }

    @Override
    public void decrementReference() {
        referenceCount--;
        if (referenceCount == 0)
            cleanup();
    }

    public void render(ShaderProgram program, String textureUniform) {
        unbind();
        clear();
        program.use();
        var mat4 = new Matrix4f();
        program.setProjectionMatrix(mat4);
        program.setViewMatrix(mat4);
        program.setModelMatrix(mat4);
        texture.bind(0);
        program.setInt(textureUniform, 0);
        Mesh.getDefaultUnitZPlane().render();
    }

    public static class FramebufferTexture2D extends Texture2D {

        protected int usage;

        public FramebufferTexture2D(int width, int height, int usage) {
            this(width, height, usage, GL_RGBA, GL_RGBA, GL_UNSIGNED_BYTE);
        }

        public FramebufferTexture2D(int width, int height, int usage, int format, int internalFormat, int dataType) {
            super(width, height, format, internalFormat, dataType);
            this.usage = usage;
        }

        protected int getTextureId() {
            return super.textureId;
        }

        protected int getUsage() {
            return usage;
        }
    }
}
