package com.crossly.components;

import com.crossly.components.subcomponents.Texture;
import com.crossly.interfaces.Component;
import static org.lwjgl.stb.STBImage.*;

import static org.lwjgl.opengl.GL46.*;

public class Texture2D extends Texture implements Component {

    protected int width, height, referenceCount = 0;

    public Texture2D(String imagePath, boolean flipVertical) {
        super(GL_TEXTURE_2D);
        use();
        int[] w = new int[1], h = new int[1], ch = new int[1];
        stbi_set_flip_vertically_on_load(flipVertical);
        var buffer = stbi_load(imagePath, w, h, ch, 0);
        if (buffer == null)
            throw new RuntimeException(stbi_failure_reason());
        width = w[0];
        height = h[0];
        glTexParameteri(super.target, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(super.target, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexImage2D(super.target, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        glGenerateMipmap(super.target);
        unbind();
        stbi_image_free(buffer);
    }

    public Texture2D(int width, int height) {
        this(width, height, GL_RGBA);
    }

    public Texture2D(int width, int height, int formats) {
        this(width, height, formats, formats, GL_UNSIGNED_BYTE);
    }

    public Texture2D(int width, int height, int format, int internalFormat, int dataType) {
        super(GL_TEXTURE_2D);
        this.width = width;
        this.height = height;
        use();
        glTexParameteri(super.target, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(super.target, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexImage2D(super.target, 0, format, width, height, 0, internalFormat, dataType, 0);
        glGenerateMipmap(super.target);
        unbind();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public static void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    @Override
    public void cleanup() {
        if (referenceCount == 0)
            glDeleteTextures(super.textureId);
        else
            decrementReference();
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

}
