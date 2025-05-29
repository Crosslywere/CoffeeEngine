package com.crossly.components;

import com.crossly.components.subcomponents.Mesh;
import com.crossly.interfaces.Component;
import com.crossly.interfaces.SubComponent;
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

    protected static final Mesh SCREEN_MESH = new Mesh(
            new float[] {
                    -1f,-1f, 0,
                     1f,-1f, 0,
                     1f, 1f, 0,
                    -1f, 1f, 0,
            },
            null,
            null,
            null,
            new int[] {
                    0, 1, 2,
                    0, 2, 3,
            }
    );

    protected static final ShaderProgram SCREEN_SHADER = ShaderProgram.builder()
            .attachVertexShader("""
                    #version 330 core
                    layout (location = 0) in vec3 a_Pos;
                    out vec2 i_TexCoord;
                    void main() {
                        i_TexCoord = (a_Pos.xy + 1.0) / 2.0;
                        gl_Position = vec4(a_Pos, 1.0);
                    }""")
            .attachFragmentShader("""
                    #version 330 core
                    out vec4 o_Color;
                    uniform sampler2D u_Texture;
                    in vec2 i_TexCoord;
                    void main() {
                        o_Color = texture2D(u_Texture, i_TexCoord);
                    }""")
            .build();

    protected final int framebufferId, width, height;
    protected final Texture texture;
    protected final RenderBuffer renderbuffer;
    protected int referenceCount = 0;

    public Framebuffer(int width, int height) {
        this.width = width;
        this.height = height;
        framebufferId = glGenFramebuffers();
        bind();
        // Color Attachment 0
        texture = new Texture(width, height, GL_RGBA, GL_COLOR_ATTACHMENT0);
        glFramebufferTexture2D(GL_FRAMEBUFFER, texture.usage(), GL_TEXTURE_2D, texture.textureId(), 0);
        // Depth stencil Renderbuffer
        renderbuffer = new RenderBuffer(width, height, GL_DEPTH24_STENCIL8, GL_DEPTH_STENCIL_ATTACHMENT);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, renderbuffer.usage(), GL_RENDERBUFFER, renderbuffer.renderBufferId());
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
        unbind();
        clear();
        SCREEN_SHADER.use();
        texture.bind();
        SCREEN_SHADER.setInt("u_Texture", texture.index);
        SCREEN_MESH.render();
    }

    @Override
    public void incrementReference() {
        referenceCount++;
    }

    @Override
    public void decrementReference() {
        referenceCount--;
        if (referenceCount <= 0)
            cleanup();
    }

    public void render(ShaderProgram program, String textureUniform) {
        unbind();
        clear();
        program.use();
        texture.bind();
        program.setInt(textureUniform, texture.index);
        SCREEN_MESH.render();
    }

    public static class Texture implements SubComponent {
        int textureId;
        int index;
        static int indexer = 0;
        int usage;
        Texture(int width, int height, int format, int usage) {
            this.usage = usage;
            index = indexer++;
            textureId = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, textureId);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, 0);
            glBindTexture(GL_TEXTURE_2D, 0);
        }
        Texture(int width, int height, int internalFormat, int format, int usage) {
            this.usage = usage;
            index = indexer++;
            textureId = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, textureId);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, GL_UNSIGNED_BYTE, 0);
            glBindTexture(GL_TEXTURE_2D, 0);
        }
        int textureId() {
            return textureId;
        }
        int usage() {
            return usage;
        }
        void bind() {
            glActiveTexture(GL_TEXTURE0 + index);
            glBindTexture(GL_TEXTURE_2D, textureId);
        }
        @Override
        public void cleanup() {
            glDeleteTextures(textureId);
        }
    }

    public static class RenderBuffer implements SubComponent {
        int renderBufferId;
        int usage;
        RenderBuffer(int width, int height, int format, int usage) {
            this.usage = usage;
            renderBufferId = glGenRenderbuffers();
            glBindRenderbuffer(GL_RENDERBUFFER, renderBufferId);
            glRenderbufferStorage(GL_RENDERBUFFER, format, width, height);
            glBindRenderbuffer(GL_RENDERBUFFER, 0);
        }
        int renderBufferId() {
            return renderBufferId;
        }
        int usage() {
            return usage;
        }
        @Override
        public void cleanup() {
            glDeleteRenderbuffers(renderBufferId);
        }
    }
}
