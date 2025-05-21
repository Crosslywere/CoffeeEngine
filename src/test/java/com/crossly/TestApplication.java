package com.crossly;

import com.crossly.components.Framebuffer;
import com.crossly.components.Mesh;
import com.crossly.components.ShaderProgram;
import com.crossly.entity.Camera3D;
import com.crossly.entity.Entity;
import com.crossly.input.Input;
import com.crossly.interfaces.Application;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class TestApplication extends Application {

    private final Entity entity;
    private final Camera3D camera;

    public TestApplication() {
        super(800, 600, "Test Application!");
        ShaderProgram testTreeShader = ShaderProgram.builder()
                .attachVertexShaderFile("test/shader/shader.vert")
                .attachFragmentShaderFile("test/shader/shader.frag")
                .build();
        testTreeShader.use();
        testTreeShader.setProjectionUniform("proj");
        testTreeShader.setModelUniform("model");
        Mesh.Buffer testMeshBuffer = Mesh.Buffer.create("test/mesh/ChessPiece.obj");
        entity = new Entity(testTreeShader, testMeshBuffer);
        camera = new Camera3D((float) getWindowWidth() / getWindowHeight(), 60);
    }

    public void onUpdate(Input input) {
        if (input.isKeyPressed(Input.KEY_ESCAPE)) quit();
        if (input.isKeyJustPressed(Input.KEY_F)) setFullscreen(!getFullscreen());
        entity.getTransform().setRotationY((float) glfwGetTime());
    }

    public void onRender() {
        Framebuffer.clear();
        entity.getShaderComponent().ifPresent(
                shaderProgram -> shaderProgram.setProjectionMatrix(camera.getProjectionView())
        );
        entity.render();
    }

    @Override
    public void onExit() {
        entity.cleanup();
    }

    @Override
    public void onResize() {
        camera.setAspectRatio((float) getWindowWidth() / getWindowHeight());
    }

    public static void main(String... args) {
        CoffeeEngine.run(TestApplication.class);
    }
}
