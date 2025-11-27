package com.crossly.coffee_engine.core;

/**
 * @author Jude Ogboru
 */
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
