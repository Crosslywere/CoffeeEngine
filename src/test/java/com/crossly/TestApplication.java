package com.crossly;

import com.crossly.components.FrameBuffer;
import com.crossly.components.Mesh;
import com.crossly.components.ShaderProgram;
import com.crossly.entity.Entity;
import com.crossly.input.Input;
import com.crossly.interfaces.Application;
import org.joml.Matrix4f;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class TestApplication extends Application {

    private final Entity entity;

    public TestApplication() {
        super(800, 600, "Test Application!");
        ShaderProgram testTreeShader = new ShaderProgram(
                """
                        #version 460 core
                        layout (location = 0) in vec3 aPos;
                        layout (location = 3) in vec3 aTexCoord;
                        uniform mat4 proj;
                        uniform mat4 model;
                        out vec3 texCoord;
                        void main() {
                            texCoord = aTexCoord;
                            gl_Position = proj * model * vec4(aPos, 1.0);
                        }""",
                """
                        #version 460 core
                        layout (location = 0) out vec4 oFragColor;
                        in vec3 texCoord;
                        void main() {
                            oFragColor = vec4(abs(texCoord), 1.0);
                        }"""
        );
        Matrix4f projection = new Matrix4f()
                .perspective((float) Math.toRadians(60), 4f / 3f, 0.1f, 100)
                .translate(0, -1, -4).rotateX((float) Math.toRadians(30));
        testTreeShader.use();
        testTreeShader.setProjectionUniform("proj");
        testTreeShader.setModelUniform("model");
        testTreeShader.setProjectionMatrix(projection);
        Mesh.Buffer testMeshBuffer = Mesh.Buffer.create("test/mesh/ChessPiece.obj");
        entity = new Entity(testTreeShader, testMeshBuffer);
    }

    public void onUpdate(Input input) {
        if (input.isKeyPressed(Input.KEY_ESCAPE)) quit();
        if (input.isKeyJustPressed(Input.KEY_F)) setFullscreen(!getFullscreen());
        entity.getTransform().setRotationY((float) glfwGetTime());
    }

    public void onRender() {
        FrameBuffer.clear();
        entity.render();
    }

    @Override
    public void onExit() {
        entity.cleanup();
    }

    public static void main(String... args) {
        CoffeeEngine.run(TestApplication.class);
    }
}
