package com.crossly.coffee_engine.component.graphics;

import org.joml.*;

import static org.lwjgl.opengl.GL20.*;

/**
 * @author Jude Ogboru
 */
public class GraphicsShader extends Shader {

	public GraphicsShader(String vertexSource, String fragmentSource) {
		int vs = compileShaderSource(vertexSource, GL_VERTEX_SHADER);
		int fs = compileShaderSource(fragmentSource, GL_FRAGMENT_SHADER);
		handle = createShaderProgram(vs, fs);
		glDeleteShader(vs);
		glDeleteShader(fs);
	}

	public void use() {
		glUseProgram(handle);
	}

	public void setInt(String uniformName, int v) {
		use();
		glUniform1i(getUniformLocation(uniformName), v);
	}

	public void setBool(String uniformName, boolean v) {
		setInt(uniformName, v ? 1 : 0);
	}

	public void setFloat(String uniformName, float v) {
		use();
		glUniform1f(getUniformLocation(uniformName), v);
	}

	public void setFloat2(String uniformName, Vector2f v2f) {
		glUniform2f(getUniformLocation(uniformName), v2f.x(), v2f.y());
	}

	public void setFloat3(String uniformName, Vector3f v3f) {
		use();
		glUniform3f(getUniformLocation(uniformName), v3f.x(), v3f.y(), v3f.z());
	}

	public void setFloat4(String uniformName, Vector4f v3f) {
		use();
		glUniform4f(getUniformLocation(uniformName), v3f.x(), v3f.y(), v3f.z(), v3f.w());
	}

	public void setMatrix2f(String uniformName, Matrix2f m2f) {
		float[] matrix = new float[4];
		m2f.get(matrix);
		use();
		glUniformMatrix2fv(getUniformLocation(uniformName), false, matrix);
	}

	public void setMatrix3f(String uniformName, Matrix3f m3f) {
		float[] matrix = new float[9];
		m3f.get(matrix);
		use();
		glUniformMatrix3fv(getUniformLocation(uniformName), false, matrix);
	}

	public void setMatrix4f(String uniformName, Matrix4f m4f) {
		float[] matrix = new float[16];
		m4f.get(matrix);
		use();
		glUniformMatrix4fv(getUniformLocation(uniformName), false, matrix);
	}

	public void setTexture(String uniformName, Texture texture, int index) {
		texture.bind(index);
		setInt(uniformName, index);
	}
}
