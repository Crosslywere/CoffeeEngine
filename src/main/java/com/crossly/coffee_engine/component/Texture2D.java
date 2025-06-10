package com.crossly.coffee_engine.component;

import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;

public final class Texture2D extends Texture {

    private final int width, height;

    public Texture2D(String imagePath) {
        this(imagePath, false);
    }

    public Texture2D(String imagePath, boolean flipVertical) {
        glBindTexture(GL_TEXTURE_2D, handle);
        int[] w = new int[1], h = new int[1], ch = new int[1];
        stbi_set_flip_vertically_on_load(flipVertical);
        var buffer = stbi_load(imagePath, w, h, ch, 0);
        if (buffer == null)
            throw new RuntimeException(stbi_failure_reason());
        width = w[0];
        height = h[0];
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        glGenerateMipmap(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, 0);
        stbi_image_free(buffer);
    }

    public Texture2D(int width, int height) {
        this(width, height, GL_RGBA);
    }

    public Texture2D(int width, int height, int formats) {
        this(width, height, formats, formats, GL_UNSIGNED_BYTE);
    }

    public Texture2D(int width, int height, int format, int internalFormat, int dataType) {
        this.width = width;
        this.height = height;
        glBindTexture(GL_TEXTURE_2D, handle);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, internalFormat, dataType, 0);
        glGenerateMipmap(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public void bind(int index) {
        glActiveTexture(GL_TEXTURE0 + index);
        glBindTexture(GL_TEXTURE_2D, handle);
    }
}
