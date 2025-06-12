package com.crossly.coffee_engine.component;

import static org.lwjgl.opengl.GL46.*;

public abstract class Texture extends SharedComponent {

    protected final int handle;

    protected Texture() {
        handle = glGenTextures();
    }

    public abstract void bind(int index);

    int getTextureID() {
        return handle;
    }

    @Override
    public void cleanup() {
        glDeleteTextures(handle);
    }
}
