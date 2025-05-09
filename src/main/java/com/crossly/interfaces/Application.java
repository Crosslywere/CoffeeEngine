package com.crossly.interfaces;

import com.crossly.CoffeeEngine;
import com.crossly.input.Input;

import static org.lwjgl.opengl.GL11C.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.glClear;

public abstract class Application {
    private int windowWidth;
    private int windowHeight;
    private final String windowTitle;
    private boolean fullscreen = false;
    private boolean running = true;

    public Application(int w, int h, String t) {
        this.windowWidth = w;
        this.windowHeight = h;
        this.windowTitle = t;
    }

    public final int getWindowWidth() {
        return windowWidth;
    }

    public final int getWindowHeight() {
        return windowHeight;
    }

    public final void setWindowDimensions(int w, int h) {
        this.windowWidth = w;
        this.windowHeight = h;
        CoffeeEngine.setWindowDimensions(this);
    }

    public final String getWindowTitle() {
        return windowTitle;
    }

    public final boolean isFullscreen() {
        return fullscreen;
    }

    public final void setFullscreen(boolean fullscreen) {
        if (this.fullscreen != fullscreen) {
            this.fullscreen = fullscreen;
            CoffeeEngine.setWindowDimensions(this);
        }
    }

    public final boolean isRunning() {
        return running;
    }

    public final void quit() {
        running = false;
    }

    public void onExit() {
    }

    public void onResize() {
    }

    public void onRender() {
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public void onUpdate(Input input) {
        if (input.isKeyPressed(Input.KEY_ESCAPE)) quit();
        if (input.isKeyJustPressed(Input.KEY_F)) setFullscreen(!fullscreen);
    }
}
