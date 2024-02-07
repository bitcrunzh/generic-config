package com.bitcrunzh.generic.config.value.java;

import java.util.Optional;

public class SimpleValue<T> implements Value<T> {
    private final T value;

    public SimpleValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
