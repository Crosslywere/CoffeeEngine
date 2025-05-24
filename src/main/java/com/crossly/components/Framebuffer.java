package com.crossly.components;

import com.crossly.interfaces.Component;

import static org.lwjgl.opengl.GL46.*;

public class Framebuffer implements Component {

    public static void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
    }

    private static final Mesh SCREEN = new Mesh(
            new float[] {
                    -1f, -1f, 0,
                    1f, -1f, 0,
                    1f,  1f, 0,
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

    private static final ShaderProgram SCREEN_SHADER = ShaderProgram.builder()
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

    private final int framebufferId, textureId, renderBufferId, width, height;

    public Framebuffer(int width, int height) {
        this.width = width;
        this.height = height;
        framebufferId = glGenFramebuffers();
        bind();
        // Color Attachment 0
        textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, textureId, 0);
        // Depth stencil Renderbuffer
        renderBufferId = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, renderBufferId);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH24_STENCIL8, width, height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_RENDERBUFFER, renderBufferId);
        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            throw new RuntimeException("Framebuffer incomplete!");
        }
        unbind();
    }

    @Override
    public void cleanup() {
        glDeleteTextures(textureId);
        glDeleteRenderbuffers(renderBufferId);
        glDeleteFramebuffers(framebufferId);
    }

    private void bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, framebufferId);
    }

    private static void unbind() {
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
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureId);
        SCREEN_SHADER.setInt("u_Texture", 0);
        SCREEN.render();
    }
}
