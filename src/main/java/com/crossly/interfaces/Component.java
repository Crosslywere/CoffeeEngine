package com.crossly.interfaces;

public interface Component {

    default void use() {}

    default void render() { use(); }

    void cleanup();
}
