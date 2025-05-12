package com.crossly.interfaces;

public interface Component {

    default void use() {}

    void cleanup();
}
