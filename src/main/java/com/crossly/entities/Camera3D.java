package com.crossly.entities;

import com.crossly.interfaces.Camera;
import org.joml.Matrix4f;

public class Camera3D extends Entity implements Camera {

    private Matrix4f projection;

    private float aspectRatio;
    private float fieldOfView;

    public Camera3D() {
        this(4f / 3f, 45);
    }

    public Camera3D(float aspectRatio, float fieldOfView) {
        this.aspectRatio = aspectRatio;
        this.fieldOfView = fieldOfView;
        getTransform().setPositionY(-1);
        getTransform().setPositionZ(-5);
        getTransform().setRotationX((float) Math.toRadians(30));
        setProjection();
    }

    private void setProjection() {
        projection = new Matrix4f().perspective((float) Math.toRadians(fieldOfView), aspectRatio, 0.1f, 1000);
    }

    @Override
    public Matrix4f getProjection() {
        return projection;
    }

    @Override
    public Matrix4f getView() {
        return getTransform().getLocalTransformMatrix();
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
        setProjection();
    }

    public float getFieldOfView() {
        return fieldOfView;
    }

    public void setFieldOfView(float fieldOfView) {
        this.fieldOfView = fieldOfView;
        setProjection();
    }
}
