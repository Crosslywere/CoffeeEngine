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
    private final Orientation orientation;

    private Transform parent = null;

    public Transform() {
        orientation = new Orientation(rotation);
    }

    public Transform(Vector3f position, Vector3f rotation, Vector3f scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        orientation = new Orientation(rotation);
    }

    public static Vector3f getWorldUp() {
        return new Vector3f(Orientation.WORLD_UP);
    }

    private static Vector3f rotate(Vector3f vector, Vector3f rotation) {
        return vector.rotate(new Quaternionf().rotateXYZ(rotation.x(), rotation.y(), rotation.z()));
    }

    public Vector3f getFront() {
        return orientation.getFront();
    }

    public Vector3f getUp() {
        return orientation.getUp();
    }

    public Vector3f getRight() {
        return orientation.getRight();
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
        return new Vector3f(position);
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
        return new Vector3f(rotation);
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
        orientation.update(rotation);
    }

    public void incrementRotation(Vector3f rotation) {
        this.rotation.add(rotation);
        orientation.update(rotation);
    }

    public float getPitch() {
        return rotation.x();
    }

    public void setPitch(float x) {
        rotation.x = x;
        orientation.update(rotation);
    }

    public float getYaw() {
        return rotation.y();
    }

    public void setYaw(float y) {
        rotation.y = y;
        orientation.update(rotation);
    }

    public float getRoll() {
        return rotation.z();
    }

    public void setRoll(float z) {
        rotation.z = z;
        orientation.update(rotation);
    }

    public Vector3f getScale() {
        return new Vector3f(scale);
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

    public static class Orientation {
        private final Vector3f front = new Vector3f();
        private final Vector3f right = new Vector3f();
        private final Vector3f up = new Vector3f();

        private static final Vector3f WORLD_UP = new Vector3f(0, 1, 0);
        private static final Vector3f WORLD_RIGHT = new Vector3f(1, 0, 0);
        private static final Vector3f WORLD_FRONT = new Vector3f(0, 0, 1);

        private Orientation(Vector3f rotation) {
            update(rotation);
        }

        private Vector3f getFront() {
            return new Vector3f(front);
        }

        private Vector3f getRight() {
            return new Vector3f(right);
        }

        private Vector3f getUp() {
            return new Vector3f(up);
        }

        private void update(Vector3f rotation) {
            update(rotation.x(), rotation.y(), rotation.z());
        }

        public void update(float pitch, float yaw, float roll) {
            Vector3f front = new Vector3f(0, 0, 1);
            front.rotateX(pitch);
            front.rotateY(yaw);
            front.normalize(this.front);
            front.cross(WORLD_UP, right);
            right.rotateZ(roll);
            right.normalize();
            right.cross(front, up);
            up.normalize();
        }
    }
}
