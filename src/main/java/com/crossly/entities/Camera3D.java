package com.crossly.entities;

import com.crossly.CoffeeEngine;
import com.crossly.components.Framebuffer;
import com.crossly.components.ShaderProgram;
import com.crossly.interfaces.Camera;
import com.crossly.util.Pair;
import org.joml.Matrix4f;

import java.util.Optional;

/**
 * @author Jude Ogboru
 */
public class Camera3D extends Entity implements Camera {

    private Matrix4f projection;

    private float aspectRatio;
    private float fieldOfView = (float) Math.toRadians(45);
    private Framebuffer framebuffer;
    private Pair<ShaderProgram, String> shaderTexturePair = null;

    public Camera3D(int width, int height) {
        aspectRatio = (float) width / height;
        getTransform().setPositionY(2.5f);
        getTransform().setPositionZ(-2.5f);
        getTransform().setPitch((float) Math.toRadians(35));
        framebuffer = new Framebuffer(width, height);
        setupProjection();
    }

    private void setupProjection() {
        projection = new Matrix4f().perspective(fieldOfView, aspectRatio, 0.1f, 1000);
    }

    @Override
    public Matrix4f getProjection() {
        return projection;
    }

    @Override
    public Matrix4f getView() {
        return new Matrix4f().lookAt(getTransform().getPosition(),
                getTransform().getPosition().add(getTransform().getFront()),
                getTransform().getUp());
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
        setupProjection();
    }

    public float getFieldOfView() {
        return fieldOfView;
    }

    public void setFieldOfView(float fieldOfView) {
        this.fieldOfView = fieldOfView;
        setupProjection();
    }

    @Override
    public void makeActive() {
        if (framebuffer != null) {
            framebuffer.use();
            Framebuffer.clear();
        }
        CoffeeEngine.setCurrentActiveCamera(this);
    }

    @Override
    public void cleanup() {
        framebuffer.cleanup();
    }

    public void drawFramebuffer() {
        framebuffer.render();
    }

    @Override
    public void render() {
        if (shaderTexturePair == null) {
            framebuffer.render();
        } else {
            framebuffer.render(shaderTexturePair.getFirst(), shaderTexturePair.getSecond());
        }
    }

    public void remake(int width, int height) {
        framebuffer.cleanup();
        framebuffer = new Framebuffer(width, height);
        aspectRatio = (float) width / height;
        setupProjection();
    }

    public Optional<ShaderProgram> getScreenShader() {
        if (shaderTexturePair != null)
            return Optional.of(shaderTexturePair.getFirst());
        return Optional.empty();
    }

    public void setScreenShader(ShaderProgram shader, String textureUniform) {
        if (shader == null || textureUniform == null || textureUniform.isBlank()) {
            shaderTexturePair = null;
        } else {
            shaderTexturePair = new Pair<>(shader, textureUniform);
        }
    }
}
