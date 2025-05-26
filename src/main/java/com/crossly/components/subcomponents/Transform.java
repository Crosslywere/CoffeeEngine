package com.crossly.components.subcomponents;

import com.crossly.interfaces.SubComponent;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * @author Jude Ogboru
 */
public class Transform implements SubComponent {

    private Vector3f position = new Vector3f();
    private Vector3f rotation = new Vector3f();
    private Vector3f scale = new Vector3f(1);

    private Transform parent = null;

    public Transform() {
    }

    private static Vector3f rotate(Vector3f vector, Vector3f rotation) {
        return vector.rotate(new Quaternionf().rotateXYZ(rotation.x(), rotation.y(), rotation.z()));
    }

    public Vector3f getForward() {
        return rotate(new Vector3f(0, 0, 1), rotation);
    }

    public Vector3f getUp() {
        return rotate(new Vector3f(0, 1, 0), rotation);
    }

    public Vector3f getRight() {
        return rotate(new Vector3f(1, 0, 0), rotation);
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

    public void incrementPosition(Vector3f offset) {
        position.add(offset);
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

    public void incrementRotation(Vector3f rotation) {
        this.rotation.add(rotation);
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

    public void incrementScale(Vector3f scale) {
        this.scale.add(scale);
    }

    public void setScale(float s) {
        scale = new Vector3f(s);
    }

    public void incrementScale(float s) {
        scale.add(s, s, s);
    }

    public void setParent(Transform parent) {
        this.parent = parent;
    }

    @Override
    public void reset() {
        position = new Vector3f();
        rotation = new Vector3f();
        scale = new Vector3f(1);
    }

    @Override
    public String toString() {
        return "TRANSLATION {" + position.x() + ", " + position.y() + ", " + position.z() + "}\n" +
                "ROTATION {" + (float) Math.toDegrees(rotation.x()) + ", " + (float) Math.toDegrees(rotation.y()) + ", " + (float) Math.toDegrees(rotation.z()) + "}\n" +
                "SCALE {" + scale.x() + ", " + scale.y() + ", " + scale.z() + "}";
    }
}
