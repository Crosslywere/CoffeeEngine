package com.crossly.coffee_engine.component.graphics;

import static org.lwjgl.opengl.GL43.*;

/**
 * @author Jude Ogboru
 */
public class ComputeShader extends Shader {

	public ComputeShader(String computeSource) {
		int cs = compileShaderSource(computeSource, GL_COMPUTE_SHADER);
		handle = createShaderProgram(cs);
		glDeleteShader(cs);
	}

	public void use(int x, int y, int z) {

	}

}
