package com.crossly.interfaces;

/**
 * @author Jude Ogboru
 */
public interface Component {

    default void use() {}

    default void render() { use(); }

    void cleanup();

    void incrementReference();

    void decrementReference();

    default Component getReference() {
        incrementReference();
        return this;
    }
}
