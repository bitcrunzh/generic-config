package com.bitcrunzh.generic.config.value.java;

import java.util.List;

public class ListValue<T> implements Value {
    private final List<Value> value;

    public ListValue(List<Value> value) {
        this.value = value;
    }

    public List<Value> getValue() {
        return value;
    }
}
