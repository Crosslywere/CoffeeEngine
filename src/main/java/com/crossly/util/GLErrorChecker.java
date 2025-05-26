package com.crossly.util;

import static org.lwjgl.opengl.GL46.*;

/**
 * @author Jude Ogboru
 */
public class GLErrorChecker {

    /**
     * Can be called after any {@code OpenGL} call to see what possibly went wrong.
     */
    public static void checkError() {
        switch (glGetError()) {
            case GL_INVALID_ENUM -> throw new RuntimeException("Invalid enum type used!");
            case GL_INVALID_VALUE -> throw new RuntimeException("Invalid value used!");
            case GL_INVALID_OPERATION -> throw new RuntimeException("Invalid operation!");
            case GL_INVALID_FRAMEBUFFER_OPERATION -> throw new RuntimeException("Invalid framebuffer operation!");
            case GL_OUT_OF_MEMORY -> throw new RuntimeException("OpenGL out of memory!");
            case GL_STACK_UNDERFLOW -> throw new RuntimeException("OpenGL stack underflow!");
            case GL_STACK_OVERFLOW -> throw new RuntimeException("OpenGL stack overflow!");
        }
    }
}
