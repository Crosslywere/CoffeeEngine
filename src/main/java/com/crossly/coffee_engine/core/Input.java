package com.crossly.coffee_engine.core;

import org.joml.Vector2i;

import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;

public class Input {
    public static final int MOUSE_BUTTON_LEFT = GLFW_MOUSE_BUTTON_LEFT,
            MOUSE_BUTTON_RIGHT = GLFW_MOUSE_BUTTON_RIGHT,
            MOUSE_BUTTON_MIDDLE = GLFW_MOUSE_BUTTON_MIDDLE,
            MOUSE_BUTTON_FORWARD = GLFW_MOUSE_BUTTON_4,
            MOUSE_BUTTON_BACK = GLFW_MOUSE_BUTTON_5;

    public static final int KEY_ESCAPE = GLFW_KEY_ESCAPE,
            KEY_F1 = GLFW_KEY_F1,
            KEY_F2 = GLFW_KEY_F2,
            KEY_F3 = GLFW_KEY_F3,
            KEY_F4 = GLFW_KEY_F4,
            KEY_F5 = GLFW_KEY_F5,
            KEY_F6 = GLFW_KEY_F6,
            KEY_F7 = GLFW_KEY_F7,
            KEY_F8 = GLFW_KEY_F8,
            KEY_F9 = GLFW_KEY_F9,
            KEY_F10 = GLFW_KEY_F10,
            KEY_F11 = GLFW_KEY_F11,
            KEY_F12 = GLFW_KEY_F12,
            KEY_GRAVE = GLFW_KEY_GRAVE_ACCENT,
            KEY_1 = GLFW_KEY_1,
            KEY_2 = GLFW_KEY_2,
            KEY_3 = GLFW_KEY_3,
            KEY_4 = GLFW_KEY_4,
            KEY_5 = GLFW_KEY_5,
            KEY_6 = GLFW_KEY_6,
            KEY_7 = GLFW_KEY_7,
            KEY_8 = GLFW_KEY_8,
            KEY_9 = GLFW_KEY_9,
            KEY_0 = GLFW_KEY_0,
            KEY_MINUS = GLFW_KEY_MINUS,
            KEY_EQUAL = GLFW_KEY_EQUAL,
            KEY_BACKSPACE = GLFW_KEY_BACKSPACE,
            KEY_A = GLFW_KEY_A,
            KEY_B = GLFW_KEY_B,
            KEY_C = GLFW_KEY_C,
            KEY_D = GLFW_KEY_D,
            KEY_E = GLFW_KEY_E,
            KEY_F = GLFW_KEY_F,
            KEY_G = GLFW_KEY_G,
            KEY_H = GLFW_KEY_H,
            KEY_I = GLFW_KEY_I,
            KEY_J = GLFW_KEY_J,
            KEY_K = GLFW_KEY_K,
            KEY_L = GLFW_KEY_L,
            KEY_M = GLFW_KEY_M,
            KEY_N = GLFW_KEY_N,
            KEY_O = GLFW_KEY_O,
            KEY_P = GLFW_KEY_P,
            KEY_Q = GLFW_KEY_Q,
            KEY_R = GLFW_KEY_R,
            KEY_S = GLFW_KEY_S,
            KEY_T = GLFW_KEY_T,
            KEY_U = GLFW_KEY_U,
            KEY_V = GLFW_KEY_V,
            KEY_W = GLFW_KEY_W,
            KEY_X = GLFW_KEY_X,
            KEY_Y = GLFW_KEY_Y,
            KEY_Z = GLFW_KEY_Z,
            KEY_SPACE = GLFW_KEY_SPACE,
            KEY_TAB = GLFW_KEY_TAB,
            KEY_CAPSLOCK = GLFW_KEY_CAPS_LOCK,
            KEY_LEFT_SHIFT = GLFW_KEY_LEFT_SHIFT,
            KEY_LEFT_CTRL = GLFW_KEY_LEFT_CONTROL,
            KEY_LEFT_ALT = GLFW_KEY_LEFT_ALT,
            KEY_RIGHT_CTRL = GLFW_KEY_RIGHT_CONTROL,
            KEY_RIGHT_ALT = GLFW_KEY_RIGHT_ALT,
            KEY_MENU = GLFW_KEY_MENU,
            KEY_RIGHT_SHIFT = GLFW_KEY_RIGHT_SHIFT,
            KEY_PERIOD = GLFW_KEY_PERIOD,
            KEY_COMMA = GLFW_KEY_COMMA,
            KEY_SLASH = GLFW_KEY_SLASH,
            KEY_SEMICOLON = GLFW_KEY_SEMICOLON,
            KEY_APOSTROPHE = GLFW_KEY_APOSTROPHE,
            KEY_LEFT_BRACKET = GLFW_KEY_LEFT_BRACKET,
            KEY_RIGHT_BRACKET = GLFW_KEY_RIGHT_BRACKET,
            KEY_BACKSLASH = GLFW_KEY_BACKSLASH,
            KEY_ENTER = GLFW_KEY_ENTER,
            KEY_LEFT_ARROW = GLFW_KEY_LEFT,
            KEY_UP_ARROW = GLFW_KEY_UP,
            KEY_RIGHT_ARROW = GLFW_KEY_RIGHT,
            KEY_DOWN_ARROW = GLFW_KEY_DOWN,
            KEY_HOME = GLFW_KEY_HOME,
            KEY_END = GLFW_KEY_END,
            KEY_INSERT = GLFW_KEY_INSERT,
            KEY_DEL = GLFW_KEY_DELETE,
            KEY_PAGE_UP = GLFW_KEY_PAGE_UP,
            KEY_PAGE_DOWN = GLFW_KEY_PAGE_DOWN,
            KEY_PRINT_SCR = GLFW_KEY_PRINT_SCREEN,
            KEY_SCROLL_LOCK = GLFW_KEY_SCROLL_LOCK,
            KEY_PAUSE = GLFW_KEY_PAUSE;

    private final static Set<Integer> keysPressed = new HashSet<>();
    private final static Set<Integer> keysLast = new HashSet<>();

    private final static Set<Integer> buttonsPressed = new HashSet<>();
    private final static Set<Integer> buttonsLast = new HashSet<>();

    private final static Vector2i mousePos = new Vector2i();

    private final static Vector2i scrollAmount = new Vector2i();

    static Window managerWindow;

    static void setKeyPressed(int keyCode, boolean action) {
        if (action) keysPressed.add(keyCode);
        else keysPressed.remove(keyCode);
    }

    static void setButtonPressed(int button, boolean action) {
        if (action) buttonsPressed.add(button);
        else buttonsPressed.remove(button);
    }

    static void setMousePos(double x, double y) {
        mousePos.x = (int) x;
        mousePos.y = (int) y;
    }

    static void setScrollAmount(double x, double y) {
        scrollAmount.x = (int) x;
        scrollAmount.y = (int) y;
    }

    static void update() {
        // Setting last frames keyboard info
        keysLast.clear();
        keysLast.addAll(keysPressed);
        // Setting last frames mouse button info
        buttonsLast.clear();
        buttonsLast.addAll(buttonsPressed);
        // Resetting the mouse scroll values
        scrollAmount.x = scrollAmount.y = 0;
    }

    static void setManagerWindow(Window window) {
        managerWindow = window;
    }

    public static boolean isKeyJustPressed(int keyCode) {
        return keysPressed.contains(keyCode) && !keysLast.contains(keyCode);
    }

    public static boolean isKeyPressed(int keyCode) {
        return keysPressed.contains(keyCode);
    }

    public static boolean isKeyJustReleased(int keyCode) {
        return !keysPressed.contains(keyCode) && keysLast.contains(keyCode);
    }

    public static boolean isButtonJustPressed(int button) {
        return buttonsPressed.contains(button) && !buttonsLast.contains(button);
    }

    public static boolean isButtonPressed(int button) {
        return buttonsPressed.contains(button);
    }

    public static boolean isButtonJustReleased(int button) {
        return !buttonsPressed.contains(button) && buttonsLast.contains(button);
    }

    public static Vector2i getMousePos() {
        return new Vector2i(mousePos);
    }

    public static boolean isScrollUp() {
        return scrollAmount.y > 0;
    }

    public static boolean isScrollDown() {
        return scrollAmount.y < 0;
    }

    public static boolean isScrollRight() {
        return scrollAmount.x < 0;
    }

    public static boolean isScrollLeft() {
        return scrollAmount.x > 0;
    }

    public static Vector2i getScrollAmount() {
        return scrollAmount.absolute(new Vector2i());
    }

    public static void captureMouse() {
        managerWindow.setMouseCaptured();
    }

    public static void disableMouse() {
        managerWindow.setMouseDisabled();
    }

    public static void hiddenMouse() {
        managerWindow.setMouseHidden();
    }

    public static void normalMouse() {
        managerWindow.setMouseNormal();
    }

    public static void forceMousePos(int x, int y) {
        managerWindow.setMousePos(x, y);
        setMousePos(x, y);
    }
}
