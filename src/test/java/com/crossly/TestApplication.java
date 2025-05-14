package com.crossly;

import com.crossly.components.ShaderProgram;
import com.crossly.input.Input;
import com.crossly.interfaces.Application;

import static org.lwjgl.opengl.GL46.*;

public class TestApplication extends Application {

    private final ShaderProgram testShader;

    public TestApplication() {
        super(800, 600, "Test Application!");
        testShader = ShaderProgram.builder()
                .attachVertexShaderFile("shader/shader.vert")
                .attachFragmentShaderFile("shader/shader.frag")
                .create();
    }

    public void onUpdate(Input input) {
        if (input.isKeyPressed(Input.KEY_ESCAPE)) quit();
        if (input.isKeyJustPressed(Input.KEY_F)) setFullscreen(!super.getFullscreen());
    }

    public void onRender() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
        testShader.use();
    }

    @Override
    public void onExit() {
        testShader.cleanup();
    }

    public static void main(String... args) {
        CoffeeEngine.run(TestApplication.class);
    }
}
