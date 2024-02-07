package com.bitcrunzh.generic.config.value.java;

import java.util.List;
import java.util.Optional;

public class ListValue<T> implements Value<List<T>> {
    private final List<Value<T>> value;

    public ListValue(List<Value<T>> value) {
        this.value = value;
    }

    public List<Value<T>> getValue() {
        return value;
    }
}
