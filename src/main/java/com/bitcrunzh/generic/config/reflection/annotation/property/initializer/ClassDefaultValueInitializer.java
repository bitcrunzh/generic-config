package com.bitcrunzh.generic.config.reflection.annotation.property.initializer;

/**
 * The implementation of this interface must have an empty constructor, allowing it to be instantiated by the framework.
 * @param <T>
 */
public interface ClassDefaultValueInitializer<T> {
    T getDefaultValue();
}
