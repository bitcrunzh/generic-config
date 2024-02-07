package com.bitcrunzh.generic.config.value.java;

import java.util.Optional;
import java.util.Set;

public class SetValue<T> implements Value<T> {
    private final Set<Value<T>> value;

    public SetValue(Set<Value<T>> value) {
        this.value = value;
    }
    public Set<Value<T>> getValue() {
        return value;
    }
}
