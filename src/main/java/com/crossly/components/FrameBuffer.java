package com.crossly.components;

import com.crossly.interfaces.Component;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL46.*;

public class FrameBuffer implements Component {

    public static void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
    }

    public static void setClearColor(Vector3f color) {
        glClearColor(color.x(), color.y(), color.z(), 1);
    }

    public void cleanup() {

    }

    public static class Builder {

    }
}
