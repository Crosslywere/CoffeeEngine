package com.crossly.coffee_engine.entity;

import java.math.BigInteger;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import com.crossly.coffee_engine.component.Component;
import com.crossly.coffee_engine.component.Transform;
/**
 * An entity that represents a camera with a view and projection matrix.
 * @author Jude Ogboru
 */
public class Camera extends Entity {

	private float fov = 60;
	private float width, height;
	private float near = .1f, far = 1000;
	private Matrix4f projection2D;
	private Matrix4f projection3D;

	public Camera(float width, float height) {
		this.width = width;
		this.height = height;
		Transform defaultTransform = new Transform();
		defaultTransform.setPosition(new Vector3f(0, 1, -5));
		addComponent(defaultTransform);
		updateProjection2D();
		updateProjection3D();
	}

	public Camera(Vector2f displayAspect) {
		this(displayAspect.x(), displayAspect.y());
	}

	public Camera(float width, float height, Component ... components) {
		this(width, height);
		addComponents(components);
	}

	public Camera(Vector2f displayAspect, Component ... components) {
		this(displayAspect);
		addComponents(components);
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

	public Matrix4f getView() {
		var transform = getComponent(Transform.class).orElse(null);
		if (transform != null)
			return new Matrix4f().lookAt(transform.getPosition(), transform.getPosition().add(transform.getFront()),
				transform.getUp());
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
}
