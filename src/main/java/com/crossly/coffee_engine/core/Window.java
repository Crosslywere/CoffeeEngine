package com.crossly.coffee_engine.core;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

/**
 * @author Jude Ogboru
 */
final class Window {

	private final long windowHandle;

	Window(Application app) {
		// Setting GLFW error callback
		GLFWErrorCallback.createPrint(System.err).set();
		if (!glfwInit())
			throw new RuntimeException("Failed to initialize GLFW!");
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		if (!app.isResizable())
			glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		long monitor = getMonitorAtIndex(app.getMonitorIndex() - 1);
		windowHandle = glfwCreateWindow(app.getWidth(), app.getHeight(), app.getTitle(),
				app.isFullscreen() ? monitor : 0, 0);
		if (windowHandle == 0) {
			glfwTerminate();
			throw new RuntimeException("Failed to create glfwWindow!");
		}
		if (!app.isFullscreen())
			centerOnMonitor(app.getWidth(), app.getHeight(), monitor);
		glfwMakeContextCurrent(windowHandle);
		GL.createCapabilities();
		setVsync(app);
		Input.setManagerWindow(this);
		Timer.init();
		glfwSetKeyCallback(windowHandle, (long window, int key, int scancode, int action, int mods) -> {
			Input.setKeyPressed(key, action >= GLFW_PRESS);
		});
		glfwSetMouseButtonCallback(windowHandle, (long window, int button, int action, int mods) -> {
			Input.setButtonPressed(button, action >= GLFW_PRESS);
		});
		glfwSetCursorPosCallback(windowHandle, (long window, double x, double y) -> {
			Input.setMousePos(x, y);
		});
		glfwSetScrollCallback(windowHandle, (long window, double x, double y) -> {
			Input.setScrollAmount(x, y);
		});
	}

	private long getMonitorAtIndex(int monitorIndex) {
		if (monitorIndex <= 0) {
			return glfwGetPrimaryMonitor();
		}
		var monitors = glfwGetMonitors();
		if (monitors == null || (monitorIndex) >= monitors.remaining()) {
			System.out.println("Monitor index is not valid. Using primary monitor.");
			return glfwGetPrimaryMonitor();
		}
		return monitors.get(monitorIndex);
	}

	private void centerOnMonitor(int width, int height, long monitor) {
		GLFWVidMode mode = glfwGetVideoMode(monitor);
		if (mode == null) {
			System.out.println("Video Mode is null!");
			return;
		}
		int[] xpos = new int[1], ypos = new int[1];
		glfwGetMonitorPos(monitor, xpos, ypos);
		glfwSetWindowPos(windowHandle, xpos[0] + (mode.width() - width) / 2, ypos[0] + (mode.height() - height) / 2);
	}

	boolean isOpen() {
		Input.update();
		Timer.update();
		glfwSwapBuffers(windowHandle);
		glfwPollEvents();
		return !glfwWindowShouldClose(windowHandle);
	}

	void setTitle(Application app) {
		glfwSetWindowTitle(windowHandle, app.getTitle());
	}

	void setWindowSize(Application app) {
		glfwSetWindowSize(windowHandle, app.getWidth(), app.getHeight());
		glViewport(0, 0, app.getWidth(), app.getHeight());
	}

	void setMonitor(Application app) {
		long monitor = getMonitorAtIndex(app.getMonitorIndex() - 1);
		GLFWVidMode mode = glfwGetVideoMode(monitor);
		if (mode == null) {
			System.out.println("Invalid monitor! Couldn't get VideoMode!");
			return;
		}
		if (app.isFullscreen()) {
			int[] xpos = new int[1], ypos = new int[1];
			glfwGetMonitorPos(monitor, xpos, ypos);
			glfwSetWindowMonitor(windowHandle, monitor, xpos[0], ypos[0], app.getWidth(), app.getHeight(),
					mode.refreshRate());
		} else {
			int[] xpos = new int[1], ypos = new int[1];
			glfwGetMonitorPos(monitor, xpos, ypos);
			glfwSetWindowMonitor(windowHandle, 0, xpos[0] + (mode.width() - app.getWidth()) / 2,
					ypos[0] + (mode.height() - app.getHeight()) / 2, app.getWidth(), app.getHeight(),
					mode.refreshRate());
			centerOnMonitor(app.getWidth(), app.getHeight(), monitor);
		}
		glViewport(0, 0, app.getWidth(), app.getHeight());
		app.onResize();
	}

	void setVsync(Application app) {
		glfwSwapInterval(app.isVsync() ? 1 : 0);
	}

	void setMouseCaptured() {
		glfwSetInputMode(windowHandle, GLFW_CURSOR, GLFW_CURSOR_CAPTURED);
	}

	void setMouseHidden() {
		glfwSetInputMode(windowHandle, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
	}

	void setMouseDisabled() {
		glfwSetInputMode(windowHandle, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
	}

	void setMouseNormal() {
		glfwSetInputMode(windowHandle, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
	}

	void setMousePos(int x, int y) {
		glfwSetCursorPos(windowHandle, x, y);
	}

	void quit() {
		glfwSetWindowShouldClose(windowHandle, true);
	}

	void cleanup() {
		glfwFreeCallbacks(windowHandle);
		glfwDestroyWindow(windowHandle);
		glfwTerminate();
	}
}
