package com.bitcrunzh.generic.config.value.java;

import java.util.Set;

public class SetValue<T> implements Value {
    private final Set<Value> value;

    public SetValue(Set<Value> value) {
        this.value = value;
    }
    public Set<Value> getValue() {
        return value;
    }
}
