package com.bitcrunzh.generic.config.value.java;

public class ArrayValue<T> implements Value {
    private final Value[] value;

    public ArrayValue(Value[] value) {
        this.value = value;
    }

    public Value[] getValue() {
        return value;
    }
}
