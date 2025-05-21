package com.crossly.components;

import static org.lwjgl.opengl.GL46.*;

public class Framebuffer {

    public static void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
    }
}
