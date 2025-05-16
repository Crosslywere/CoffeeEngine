package com.crossly.components.subcomponents;

import org.joml.AxisAngle4f;
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
        var rot = new AxisAngle4f(1, rotation);
        var matrix = new Matrix4f()
                .translate(position)
                .scale(scale)
                .rotate(new Quaternionf(rot));
        if (parent != null) {
            return parent.getTransformMatrix().mul(matrix);
        }
        return matrix;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public void setRotationY(float y) {
        rotation.y = y;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public void setParent(Transform parent) {
        this.parent = parent;
    }
}
