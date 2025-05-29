package com.crossly.interfaces;

/**
 * @author Jude Ogboru
 */
public interface SubComponent extends Component {

    default void reset() {
    }

    default void bind(int index) {
    }

    @Override
    default void use() {
        bind(0);
    }

    @Override
    default void cleanup() {
    }

    @Override
    default void incrementReference() {
    }

    @Override
    default void decrementReference() {
    }
}
