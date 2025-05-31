package com.crossly.components.subcomponents;

import com.crossly.interfaces.SubComponent;

import static org.lwjgl.opengl.GL46.*;

public class Renderbuffer implements SubComponent {

    protected int renderbufferId, usage;

    public Renderbuffer(int width, int height, int usage, int format) {
        this.usage = usage;
        renderbufferId = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, renderbufferId);
        glRenderbufferStorage(GL_RENDERBUFFER, format, width, height);
        glBindRenderbuffer(GL_RENDERBUFFER, 0);
    }

    public int getRenderbufferId() {
        return renderbufferId;
    }

    public int getUsage() {
        return usage;
    }

    @Override
    public void cleanup() {
        glDeleteRenderbuffers(renderbufferId);
    }
}
