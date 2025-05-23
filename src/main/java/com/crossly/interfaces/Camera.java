package com.crossly.interfaces;

import com.crossly.CoffeeEngine;
import org.joml.Matrix4f;

public interface Camera {

    Matrix4f getProjection();

    Matrix4f getView();

    default Matrix4f getProjectionView() {
        Matrix4f projView = new Matrix4f();
        getProjection().mul(getView(), projView);
        return projView;
    }

    default void makeActive() {
        CoffeeEngine.setCurrentActiveCamera(this);
    }
}
