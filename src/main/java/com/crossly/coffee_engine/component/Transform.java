package com.crossly.coffee_engine.component;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * @author Jude Ogboru
 */
public final class Transform extends OwnedComponent {

	private Vector3f position;
	private Vector3f rotation;
	private Vector3f scale;
	private final Orientation orientation;
	private Transform parent = null;

	public Transform() {
		position = new Vector3f();
		rotation = new Vector3f();
		scale = new Vector3f(1);
		orientation = new Orientation(this);
	}

	public Transform(Vector3f position) {
		this(position, new Vector3f());
	}

	public Transform(Vector3f position, Vector3f rotation) {
		this(position, rotation, new Vector3f(1));
	}

	public Transform(Vector3f position, Vector3f rotation, Vector3f scale) {
		this.position = new Vector3f(position);
		this.rotation = new Vector3f(rotation);
		this.scale = new Vector3f(scale);
		orientation = new Orientation(this);
	}

	public Transform(Transform transform) {
		this(transform.getPosition(), transform.getRotation(), transform.getScale());
		parent = transform.parent;
	}

	public Vector3f getPosition() {
		return new Vector3f(position);
	}

	public void setPosition(Vector3f position) {
		this.position = new Vector3f(position);
	}

	public float getPositionX() {
		return position.x();
	}

	public void setPositionX(float x) {
		position.x = x;
	}

	public float getPositionY() {
		return position.y();
	}

	public void setPositionY(float y) {
		position.y = y;
	}

	public float getPositionZ() {
		return position.z();
	}

	public void setPositionZ(float z) {
		position.z = z;
	}

	public Vector3f getRotation() {
		return new Vector3f(rotation);
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = new Vector3f(rotation);
		orientation.update(this);
	}

	private float getRadPitch() {
		return (float) Math.toRadians(rotation.x());
	}

	private float getRadYaw() {
		return (float) Math.toRadians(rotation.y());
	}

	private float getRadRoll() {
		return (float) Math.toRadians(rotation.z());
	}

	public Vector3f getScale() {
		return new Vector3f(scale);
	}

	public void setScale(Vector3f scale) {
		this.scale = new Vector3f(scale);
	}

	public float getPitch() {
		return rotation.x();
	}

	public void setPitch(float pitch) {
		rotation.x = pitch;
		orientation.update(this);
	}

	public float getYaw() {
		return rotation.y();
	}

	public void setYaw(float yaw) {
		rotation.y = yaw;
		orientation.update(this);
	}

	public float getRoll() {
		return rotation.z();
	}

	public void setRoll(float roll) {
		rotation.z = roll;
		orientation.update(this);
	}

	public Vector3f getFront() {
		return new Vector3f(orientation.front);
	}

	public Vector3f getRight() {
		return new Vector3f(orientation.right);
	}

	public Vector3f getUp() {
		return new Vector3f(orientation.up);
	}

	public void setParent(Transform transform) {
		this.parent = transform;
	}

	public Vector3f getWorldPosition() {
		var posVec4 = new Vector4f(getPosition(), 1).mul(getModelMatrix());
		return posVec4.xyz(new Vector3f());
	}

	public Matrix4f getModelMatrix() {
		var model = new Matrix4f()
				.translate(position)
				.rotate(new Quaternionf().rotateXYZ(getRadPitch(), getRadYaw(), getRadRoll()))
				.scale(scale);
		if (parent == null || parent == this /* Prevent infinite self referencing! */)
			return model;
		return parent.getModelMatrix().mul(model);
	}

	public static Vector3f getWorldUp() {
		return new Vector3f(Orientation.WORLD_UP);
	}

	@Override
	protected void cleanup() {
		this.position = new Vector3f(position);
		this.rotation = new Vector3f(rotation);
		this.scale = new Vector3f(scale);
		orientation.update(this);
	}

	private static class Orientation {
		private Vector3f front;
		private final Vector3f right;
		private final Vector3f up;
		private static final Vector3f WORLD_UP = new Vector3f(0, 1, 0);

		private Orientation(Transform transform) {
			front = new Vector3f();
			right = new Vector3f();
			up = new Vector3f();
			update(transform);
		}

		private void update(Transform transform) {
			front = new Vector3f(0, 0, 1)
					.rotateX(transform.getRadPitch())
					.rotateZ(transform.getRadYaw());
			orientFront(front, transform.getRadRoll());
		}

		private void orientFront(Vector3f dir, float roll) {
			this.front = new Vector3f(dir).normalize();
			front.cross(WORLD_UP, right);
			right.rotateZ(roll).normalize();
			right.cross(front, up);
		}
	}
}
