package com.crossly.coffee_engine.core;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Timer {

    private static float totalTime = 0f;
    private static float deltaTime = 0f;

    static void init() {
        totalTime = (float) glfwGetTime();
    }

    static void update() {
        float now;
        deltaTime = (now = (float) glfwGetTime()) - totalTime;
        totalTime = now;
    }

    public static float getDeltaTime() {
        return deltaTime;
    }

    public static float getTotalTime() {
        return totalTime;
    }
}
