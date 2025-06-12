package com.crossly.coffee_engine.component;

import org.joml.*;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL46.*;

public final class Shader extends SharedComponent {

    private final int handle;
    private final Map<String, Integer> uniformCache = new HashMap<>();

    public Shader() {
        handle = 0;
    }

    public Shader(String vertexShaderSource, String fragmentShaderSource) {
        int vs = compileShaderSource(vertexShaderSource, GL_VERTEX_SHADER);
        int fs = compileShaderSource(fragmentShaderSource, GL_FRAGMENT_SHADER);
        handle = createShaderProgram(vs, fs);
        glDeleteShader(vs);
        glDeleteShader(fs);
    }

    private static int compileShaderSource(String source, int type) {
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

    private static int createShaderProgram(int... shaders) {
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

    private int getUniformLocation(String uniformName) {
        if (uniformCache.containsKey(uniformName))
            return uniformCache.get(uniformName);
        int location = glGetUniformLocation(handle, uniformName);
        uniformCache.put(uniformName, location);
        if (location < 0) System.err.println("uniform " + uniformName + " is not valid!");
        return location;
    }

    @Override
    protected void cleanup() {
        glDeleteShader(handle);
    }
}
