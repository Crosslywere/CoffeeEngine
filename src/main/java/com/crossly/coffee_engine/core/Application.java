package com.crossly.coffee_engine.core;

import com.crossly.coffee_engine.component.StaticMesh;
import org.joml.Vector2i;

public abstract class Application {

    private Vector2i screenDimensions = new Vector2i(800, 600);
    private String title = "CoffeeEngine";
    private boolean vsync = true, fullscreen = false;
    private int monitorIndex = 0;
    private Window window = null;

    public Application() {}

    public Application(int width, int height, String title) {
        this(new Vector2i(width, height), title);
    }

    public Application(Vector2i screenDimensions, String title) {
        this.screenDimensions = screenDimensions;
        this.title = title;
    }

    public Application(Application application) {
        this.screenDimensions = application.screenDimensions;
        this.title = application.getTitle();
        this.vsync = application.isVsync();
        this.fullscreen = application.isFullscreen();
        this.monitorIndex = application.getMonitorIndex();
        this.window = application.window;
    }

    protected final void createContext() {
        if (window == null)
            window = new Window(this);
    }

    public final String getTitle() {
        return title;
    }

    public final void setTitle(String title) {
        this.title = title;
        if (window != null)
            window.setTitle(this);
    }

    public final int getWidth() {
        return screenDimensions.x();
    }

    public final int getHeight() {
        return screenDimensions.y();
    }

    public final void setScreenDimensions(Vector2i screenDimensions) {
        this.screenDimensions = screenDimensions;
        if (window != null)
            window.setWindowSize(this);
    }

    public final boolean isVsync() {
        return vsync;
    }

    public final void setVsync(boolean vsync) {
        if (this.vsync != vsync) {
            this.vsync = vsync;
            if (window != null)
                window.setVsync(this);
        }
    }

    public final boolean isFullscreen() {
        return fullscreen;
    }

    public final void setFullscreen(boolean fullscreen) {
        if (this.fullscreen != fullscreen) {
            this.fullscreen = fullscreen;
            if (window != null)
                window.setMonitor(this);
        }
    }

    public final int getMonitorIndex() {
        return monitorIndex;
    }

    public final void setMonitorIndex(int monitorIndex) {
        this.monitorIndex = monitorIndex;
        if (window != null)
            window.setMonitor(this);
    }

    public void onCreate() {}

    public void onUpdate() {}

    public void onRender() {}

    public void onExit() {}

    public void onResize() {}

    public void quit() {
        window.quit();
    }

    boolean isOpen() {
        return window != null && window.isOpen();
    }

    void cleanup() {
        StaticMesh.UNIT_RECT.delete();
        if (window != null)
            window.cleanup();
    }
}
