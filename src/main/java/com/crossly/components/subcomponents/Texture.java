package com.crossly.components.subcomponents;

import com.crossly.interfaces.SubComponent;

import static org.lwjgl.opengl.GL46.*;

public class Texture implements SubComponent {

    protected int target;
    protected int textureId;

    public Texture(int target) {
        textureId = glGenTextures();
        this.target = target;
    }

    @Override
    public void use() {
        glBindTexture(target, textureId);
    }

    @Override
    public void bind(int index) {
        glActiveTexture(GL_TEXTURE0 + index);
        use();
    }

    public void cleanup() {
        glDeleteTextures(textureId);
    }
}
