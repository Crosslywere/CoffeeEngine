package com.crossly;

import com.crossly.interfaces.Application;
import com.crossly.interfaces.Camera;
import com.crossly.timer.Timer;
import com.crossly.window.Window;

import java.util.Optional;

/**
 * @author Jude Ogboru
 */
public class CoffeeEngine {

    private static Window window = null;
    private static Camera currentActiveCamera = null;

    public static void setWindowDimensions(Application application) {
        window.setWindowDimensions(application);
    }

    public static Optional<Camera> getCurrentActiveCamera() {
        return Optional.ofNullable(currentActiveCamera);
    }

    public static void setCurrentActiveCamera(Camera camera) {
        currentActiveCamera = camera;
    }

    public static<T extends Application> void run(Class<T> applicationClass) {
        window = new Window();
        try {
            Application instance = applicationClass.getConstructor().newInstance();
            setWindowDimensions(instance);
            window.setTitle(instance.getWindowTitle());
            Timer.init();
            while (window.isOpen() && instance.isRunning()) {
                instance.onUpdate(window.getInput());
                instance.onRender();
                Timer.update();
            }
            instance.onExit();
            window.cleanup();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
