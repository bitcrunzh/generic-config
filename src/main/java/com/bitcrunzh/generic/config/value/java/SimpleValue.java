package com.bitcrunzh.generic.config.value.java;

public class SimpleValue<T> implements Value {
    private final T value;

    public SimpleValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
