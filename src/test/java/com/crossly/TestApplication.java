package com.crossly;

import com.crossly.interfaces.Application;

public class TestApplication extends Application {

    public TestApplication() {
        super(1600, 900, "Test Application!");
        setFullscreen(true);
    }

    public static void main(String... args) {
        CoffeeEngine.run(TestApplication.class);
    }
}
