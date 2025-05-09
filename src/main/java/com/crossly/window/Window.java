package com.crossly.window;

import com.crossly.input.Input;
import com.crossly.interfaces.Application;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;

public class Window {

    private final long window;
    private boolean vsync = false;

    private final Input input = new Input();

    public Window() {
        if (!glfwInit())
            throw new RuntimeException("glfwInit failed!");
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        window = glfwCreateWindow(800, 600, "Coffee Engine", 0L, 0L);
        if (window == 0)
            throw new RuntimeException("glfwCreateWindow failed!");
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glBlendEquation(GL_FUNC_ADD);
        glfwSetKeyCallback(window, new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                input.setKeyPressed(key, action >= GLFW_PRESS);
            }
        });
//        glfwSetMouseButtonCallback(window, new GLFWMouseButtonCallback() {
//            @Override
//            public void invoke(long window, int button, int action, int mods) {
//                input.setButtonPressed(button, action >= GLFW_PRESS);
//            }
//        });
//        glfwSetCursorPosCallback(window, new GLFWCursorPosCallback() {
//            @Override
//            public void invoke(long window, double x, double y) {
//                input.setMousePos(x, y);
//            }
//        });
//        glfwSetScrollCallback(window, new GLFWScrollCallback() {
//            @Override
//            public void invoke(long window, double x, double y) {
//                input.setScrollAmount(x, y);
//            }
//        });
    }

    public Input getInput() {
        return input;
    }

    public void setWindowDimensions(Application instance) {
        long monitor = glfwGetPrimaryMonitor();
        var mode = glfwGetVideoMode(monitor);
        if (mode != null && (instance.getWindowWidth() > 0 || instance.getWindowHeight() > 0)) {
            if (instance.isFullscreen()) {
                glfwSetWindowMonitor(window, monitor, 0, 0, instance.getWindowWidth(), instance.getWindowHeight(), mode.refreshRate());
            } else {
                glfwSetWindowMonitor(window, 0L, 0, 0, 800, 600, mode.refreshRate());
                glfwSetWindowSize(window, instance.getWindowWidth(), instance.getWindowHeight());
            }
            glViewport(0, 0, instance.getWindowWidth(), instance.getWindowHeight());
            instance.onResize();
        }
    }

    public void setTitle(String title) {
        glfwSetWindowTitle(window, title);
    }

    public boolean isVsync() {
        return vsync;
    }

    public void setVsync(boolean vsync) {
        this.vsync = vsync;
        glfwSwapInterval(vsync ? 1 : 0);
    }

    public boolean isOpen() {
        input.update();
        glfwSwapBuffers(window);
        glfwPollEvents();
        return !glfwWindowShouldClose(window);
    }

    public void cleanup() {
        glfwDestroyWindow(window);
        glfwTerminate();
    }
}
