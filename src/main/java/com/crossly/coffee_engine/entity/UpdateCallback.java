package com.crossly.coffee_engine.entity;

/**
 * @author Jude Ogboru
 */
public interface UpdateCallback {
	default void invoke(Entity self) {
	}
}
