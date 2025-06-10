package com.crossly.coffee_engine.entity;

import com.crossly.coffee_engine.component.Framebuffer;
import com.crossly.coffee_engine.component.Shader;
import com.crossly.coffee_engine.component.Transform;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.math.BigInteger;

public class Camera extends Entity {

    private float fov = 60;
    private float width, height;
    private float near = .1f, far = 1000;
    private Matrix4f projection2D;
    private Matrix4f projection3D;
    private Framebuffer.Builder renderConfig = null;

    public Camera(float width, float height) {
        this.width = width;
        this.height = height;
        Transform defaultTransform = new Transform();
        defaultTransform.setPosition(new Vector3f(0, 1, -5));
        addComponent(defaultTransform);
        updateProjection2D();
        updateProjection3D();
        rebuildFramebuffers();
    }

    public Camera(Vector2f displayAspect) {
        this(displayAspect.x(), displayAspect.y());
    }

    public Camera(float width, float height, Shader shader, Framebuffer.Builder config) {
        this(width, height);
        renderConfig = config;
        addComponent(shader);
        addComponent(config.build((int) width, (int) height));
    }

    public Camera(Vector2f displayAspect, Shader shader, Framebuffer.Builder config) {
        this(displayAspect);
        renderConfig = config;
        addComponent(shader);
        addComponent(config.build((int) width, (int) height));
    }

    public Matrix4f getProjection2D() {
        return projection2D;
    }

    private void updateProjection2D() {
        var gcd = BigInteger.valueOf((int) width).gcd(BigInteger.valueOf((int) height)).intValue();
        float left = -(width / gcd) / 2,
                right = (width / gcd) / 2,
                bottom = -(height / gcd) / 2,
                top = (height / gcd) / 2;
        projection2D = new Matrix4f().ortho(left, right, bottom, top, near, far);
    }

    public Matrix4f getProjection3D() {
        return projection3D;
    }

    private void updateProjection3D() {
        projection3D = new Matrix4f().perspective((float) Math.toRadians(fov), width / height, near, far);
    }

    public void rebuildFramebuffers() {
        removeComponent(Framebuffer.class);
        addComponent(new Framebuffer((int) width, (int) height));
    }

    public Matrix4f getView() {
        var transform = getComponent(Transform.class).orElse(null);
        if (transform != null)
            return new Matrix4f().lookAt(transform.getPosition(), transform.getPosition().add(transform.getFront()), transform.getUp());
        return new Matrix4f();
    }

    public Vector2f getDisplayAspect() {
        return new Vector2f(width, height);
    }

    public void setDisplayAspect(float width, float height) {
        this.width = width;
        this.height = height;
        updateProjection2D();
        updateProjection3D();
        if (renderConfig != null) {
            removeComponent(Framebuffer.class);
            addComponent(renderConfig.build((int) width, (int) height));
        }
    }

    public void setDisplayAspect(Vector2f displayAspect) {
        setDisplayAspect(displayAspect.x(), displayAspect.y());
    }

    public float getFov() {
        return fov;
    }

    public void setFov(float fov) {
        this.fov = fov;
        updateProjection3D();
    }

    public float getNear() {
        return near;
    }

    public void setNear(float near) {
        this.near = near;
        updateProjection3D();
    }

    public float getFar() {
        return far;
    }

    public void setFar(float far) {
        this.far = far;
        updateProjection3D();
    }

    public void bindFramebuffer() {
        if (getComponent(Shader.class).isPresent())
            getComponent(Framebuffer.class).ifPresent(Framebuffer::bind);
    }
}
