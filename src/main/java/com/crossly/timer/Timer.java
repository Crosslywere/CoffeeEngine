package com.crossly.timer;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

/**
 * @author Jude Ogboru
 */
public class Timer {

    private static float totalTime = 0f;
    private static float deltaTime = 0f;

    public static void init() {
        totalTime = (float) glfwGetTime();
    }

    public static void update() {
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
