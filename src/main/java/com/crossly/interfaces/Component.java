package com.crossly.interfaces;

/**
 * @author Jude Ogboru
 */
public interface Component {

    default void use() {}

    default void render() { use(); }

    void cleanup();
}
