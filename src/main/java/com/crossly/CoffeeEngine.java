package com.crossly;

import com.crossly.interfaces.Application;
import com.crossly.window.Window;

public class CoffeeEngine {

    private static Window window = null;

    public static void setWindowDimensions(Application application) {
        window.setWindowDimensions(application);
    }

    public static void run(Class<?> applicationClass) {
        window = new Window();
        try {
            Object instance = applicationClass.getConstructor().newInstance();
            if (instance instanceof Application application) {
                setWindowDimensions(application);
                window.setTitle(application.getWindowTitle());
                while (window.isOpen() && application.isRunning()) {
                    application.onUpdate(window.getInput());
                    application.onRender();
                }
                application.onExit();
                window.cleanup();
            } else {
                System.err.println(applicationClass.getName() + " must extend " + Application.class.getName());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
