package com.crossly.components;

import com.crossly.interfaces.Component;
import com.crossly.util.FileUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL46.*;

public class ShaderProgram implements Component {

    private final int program;
    private final Map<String, Integer> uniformLocationCache = new HashMap<>();

    public ShaderProgram(String vertexSource, String fragmentSource) {
        this.program = builder()
                .attachVertexShader(vertexSource)
                .attachFragmentShader(fragmentSource)
                .create().program;
    }

    private ShaderProgram(int program) {
        this.program = program;
    }

    @Override
    public void use() {
        glUseProgram(program);
    }

    @Override
    public void cleanup() {
        glDeleteProgram(program);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        // To allow for multiple shader programs to be created from 1 builder create program when create is called.
        private final int program;

        private final List<Integer> shaderParts = new ArrayList<>();

        public Builder() {
            program = glCreateProgram();
        }

        public Builder attachVertexShader(String src) {
            shaderParts.add(compileShaderFromMemory(src, GL_VERTEX_SHADER));
            return this;
        }

        public Builder attachVertexShaderFile(String srcPath) {
            String src = FileUtil.getFileString(srcPath);
            return attachVertexShader(src);
        }

        public Builder attachFragmentShader(String src) {
            shaderParts.add(compileShaderFromMemory(src, GL_FRAGMENT_SHADER));
            return this;
        }

        public Builder attachFragmentShaderFile(String srcPath) {
            String src = FileUtil.getFileString(srcPath);
            return attachFragmentShader(src);
        }

        public ShaderProgram create() {
            for (int part : shaderParts) {
                glAttachShader(program, part);
            }
            glLinkProgram(program);
            validateProgram(program);
            return new ShaderProgram(program);
        }

        public ShaderProgram createCopy() {
            int program = glCreateProgram();
            for (int part : shaderParts) {
                glAttachShader(program, part);
            }
            glLinkProgram(program);
            validateProgram(program);
            return new ShaderProgram(program);
        }

        private static int compileShaderFromMemory(String src, int type) {
            int shader = glCreateShader(type);
            glShaderSource(shader, src);
            glCompileShader(shader);
            validateShader(shader);
            return shader;
        }

        private static void validateShader(int shader) {
            int[] success = new int[1];
            glGetShaderiv(shader, GL_COMPILE_STATUS, success);
            if (success[0] == 0) {
                String infoLog = glGetShaderInfoLog(shader);
                throw new RuntimeException(infoLog);
            }
        }

        private static void validateProgram(int program) {
            int[] success = new int[1];
            glGetProgramiv(program, GL_LINK_STATUS, success);
            if (success[0] == 0) {
                String infoLog = glGetProgramInfoLog(program);
                throw new RuntimeException(infoLog);
            }
        }
    }
}
