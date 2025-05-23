package com.crossly.components.subcomponents;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Transform {

    private Vector3f position = new Vector3f();
    private Vector3f rotation = new Vector3f();
    private Vector3f scale = new Vector3f(1);

    private Transform parent = null;

    public Transform() {
    }

    public Matrix4f getTransformMatrix() {
        var matrix = new Matrix4f()
                .translate(position)
                .rotate(new Quaternionf().rotateXYZ(rotation.x(), rotation.y(), rotation.z()))
                .scale(scale);
        if (parent != null) {
            return parent.getTransformMatrix().mul(matrix);
        }
        return matrix;
    }

    public Matrix4f getLocalTransformMatrix() {
        return new Matrix4f()
                .translate(position)
                .rotate(new Quaternionf().rotateXYZ(rotation.x(), rotation.y(), rotation.z()))
                .scale(scale);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setPositionX(float x) {
        position.x = x;
    }

    public void setPositionY(float y) {
        position.y = y;
    }

    public void setPositionZ(float z) {
        position.z = z;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public void setRotationX(float x) {
        rotation.x = x;
    }

    public void setRotationY(float y) {
        rotation.y = y;
    }

    public void setRotationZ(float z) {
        rotation.z = z;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public void setScale(float s) {
        scale = new Vector3f(s);
    }

    public void setParent(Transform parent) {
        this.parent = parent;
    }
}
