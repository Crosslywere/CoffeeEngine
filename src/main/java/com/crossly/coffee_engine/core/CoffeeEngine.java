package com.crossly.coffee_engine.core;

public class CoffeeEngine {

    public static void run(Application application) {
        application.onCreate();
        while (application.isOpen()) {
            application.onUpdate();
            application.onRender();
        }
        application.onExit();
        application.cleanup();
    }
}
