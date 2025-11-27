package com.crossly.coffee_engine.component.graphics;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;
import java.util.Map;

import com.crossly.coffee_engine.component.SharedComponent;

/**
 * @author Jude Ogboru
 */
public abstract class Shader extends SharedComponent {

	protected int handle;
	private final Map<String, Integer> uniformCache = new HashMap<>();

	protected static int compileShaderSource(String source, int type) {
		int shader = glCreateShader(type);
		glShaderSource(shader, source);
		glCompileShader(shader);
		validateShaderCompile(shader);
		return shader;
	}

	private static void validateShaderCompile(int shader) {
		int success = glGetShaderi(shader, GL_COMPILE_STATUS);
		if (success == GL_FALSE) {
			String infoLog = glGetShaderInfoLog(shader);
			throw new RuntimeException("Failed to compile shader:\n" + infoLog);
		}
	}

	protected static int createShaderProgram(int... shaders) {
		int program = glCreateProgram();
		for (int shader : shaders) {
			glAttachShader(program, shader);
		}
		glLinkProgram(program);
		validateProgramLink(program);
		return program;
	}

	private static void validateProgramLink(int program) {
		int success = glGetProgrami(program, GL_LINK_STATUS);
		if (success == GL_FALSE) {
			String infoLog = glGetProgramInfoLog(program);
			throw new RuntimeException("Failed to link program:\n" + infoLog);
		}
	}

	protected int getUniformLocation(String uniformName) {
		if (uniformCache.containsKey(uniformName))
			return uniformCache.get(uniformName);
		int location = glGetUniformLocation(handle, uniformName);
		uniformCache.put(uniformName, location);
		if (location < 0)
			System.err.println("uniform " + uniformName + " is not valid!");
		return location;
	}

	@Override
	protected void cleanup() {
		glDeleteShader(handle);
	}
}
