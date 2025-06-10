package com.crossly.coffee_engine.entity;

public interface UpdateCallback {
    default void invoke(Entity self) {
    }
}
