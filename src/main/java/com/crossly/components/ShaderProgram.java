package com.crossly.components;

import com.crossly.interfaces.Component;
import com.crossly.util.FileUtil;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;

import java.util.*;

import static org.lwjgl.opengl.GL46.*;

/**
 * @author Jude Ogboru
 */
public class ShaderProgram implements Component {

    private final int program;
    private final Map<String, Integer> uniformLocationCache = new HashMap<>();
    private int projectionUniformLocation = -1;
    private int viewUniformLocation = -1;
    private int modelUniformLocation = -1;

    public ShaderProgram(String vertexSource, String fragmentSource) {
        this.program = builder()
                .attachVertexShader(vertexSource)
                .attachFragmentShader(fragmentSource)
                .build().program;
    }

    private ShaderProgram(int program) {
        this.program = program;
    }

    private int getUniformLocation(String name) {
        if (!uniformLocationCache.containsKey(name)) {
            int loc = glGetUniformLocation(program, name);
            uniformLocationCache.put(name, loc);
            if (loc < 0) System.out.println("uniform '" + name + "' is unused/unavailable in shader.");
            return loc;
        }
        return uniformLocationCache.get(name);
    }

    public void setInt(String name, int value) {
        glUniform1i(getUniformLocation(name), value);
    }

    public void setInts(String name, int... values) {
        glUniform1iv(getUniformLocation(name), values);
    }

    public void setFloat(String name, float value) {
        glUniform1f(getUniformLocation(name), value);
    }

    public void setFloats(String name, float... values) {
        glUniform1fv(getUniformLocation(name), values);
    }

    public void setFloat2(String name, float x, float y) {
        glUniform2f(getUniformLocation(name), x, y);
    }

    public void setFloat2(String name, Vector2f value) {
        setFloat2(name, value.x(), value.y());
    }

    public void setFloat2s(String name, float... values) {
        assert values.length % 2 == 0;
        glUniform2fv(getUniformLocation(name), values);
    }

    public void setFloat3(String name, float x, float y, float z) {
        glUniform3f(getUniformLocation(name), x, y, z);
    }

    public void setFloat3(String name, Vector3f value) {
        setFloat3(name, value.x(), value.y(), value.z());
    }

    public void setFloat3s(String name, float... values) {
        assert values.length % 3 == 0;
        glUniform3fv(getUniformLocation(name), values);
    }

    public void setMat4(String name, float... values) {
        assert values.length == 16;
        glUniformMatrix4fv(getUniformLocation(name), false, values);
    }

    public void setMat4(String name, Matrix4f matrix) {
        float[] values = new float[16];
        matrix.get(values);
        glUniformMatrix4fv(getUniformLocation(name), false, values);
    }

    public void setProjectionUniform(String name) {
        projectionUniformLocation = getUniformLocation(name);
    }

    public void setProjectionMatrix(Matrix4f matrix) {
        if (projectionUniformLocation == -1) return;
        float[] values = new float[16];
        matrix.get(values);
        glUniformMatrix4fv(projectionUniformLocation, false, values);
    }

    public void setViewUniform(String name) {
        viewUniformLocation = getUniformLocation(name);
    }

    public void setViewMatrix(Matrix4f matrix) {
        if (viewUniformLocation == -1) return;
        float[] values = new float[16];
        matrix.get(values);
        glUniformMatrix4fv(viewUniformLocation, false, values);
    }

    public void setModelUniform(String name) {
        modelUniformLocation = getUniformLocation(name);
    }

    public void setModelMatrix(Matrix4f matrix) {
        if (modelUniformLocation == -1) return;
        float[] values = new float[16];
        matrix.get(values);
        glUniformMatrix4fv(modelUniformLocation, false, values);
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

        private String projection = null, view = null, model = null;

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

        public Builder attachComputeShader(String src) {
            shaderParts.add(compileShaderFromMemory(src, GL_COMPUTE_SHADER));
            return this;
        }

        public Builder attachComputeShaderFile(String srcPath) {
            String src = FileUtil.getFileString(srcPath);
            return attachComputeShader(src);
        }

        public Builder attachGeometryShader(String src) {
            shaderParts.add(compileShaderFromMemory(src, GL_GEOMETRY_SHADER));
            return this;
        }

        public Builder attachGeometryShaderFile(String srcPath) {
            String src = FileUtil.getFileString(srcPath);
            return attachGeometryShader(src);
        }

        public Builder attachTesselationEvaluationShader(String src) {
            shaderParts.add(compileShaderFromMemory(src, GL_TESS_EVALUATION_SHADER));
            return this;
        }

        public Builder attachTesselationEvaluationShaderFile(String srcPath) {
            String src = FileUtil.getFileString(srcPath);
            return attachTesselationEvaluationShader(src);
        }

        public Builder attachTesselationControlShader(String src) {
            shaderParts.add(compileShaderFromMemory(src, GL_TESS_CONTROL_SHADER));
            return this;
        }

        public Builder attachTesselationControlShaderFile(String srcPath) {
            String src = FileUtil.getFileString(srcPath);
            return attachTesselationControlShader(src);
        }

        public Builder setProjection(String projection) {
            this.projection = projection;
            return this;
        }

        public Builder setView(String view) {
            this.view = view;
            return this;
        }

        public Builder setModel(String model) {
            this.model = model;
            return this;
        }

        public ShaderProgram build() {
            for (int part : shaderParts) {
                glAttachShader(program, part);
            }
            glLinkProgram(program);
            validateProgram(program);
            shaderParts.forEach(GL46::glDeleteShader);
            var shader = new ShaderProgram(program);
            shader.use();
            if (projection != null) shader.setProjectionUniform(projection);
            if (view != null) shader.setViewUniform(view);
            if (model != null) shader.setModelUniform(model);
            return shader;
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
