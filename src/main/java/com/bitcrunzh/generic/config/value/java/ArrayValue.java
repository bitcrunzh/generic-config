package com.bitcrunzh.generic.config.value.java;

import java.util.Optional;

public class ArrayValue<T> implements Value<T[]> {
    private final Value<T>[] value;

    public ArrayValue(Value<T>[] value) {
        this.value = value;
    }

    public Value<T>[] getValue() {
        return value;
    }
}
