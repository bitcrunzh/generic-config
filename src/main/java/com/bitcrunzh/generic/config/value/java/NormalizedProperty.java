package com.bitcrunzh.generic.config.value.java;

import java.util.Optional;

public class NormalizedProperty<T> {
    private final String propertyName;
    private final Value value;

    public NormalizedProperty(String propertyName, Value value) {
        this.propertyName = propertyName;
        this.value = value;
    }

    public String getPropertyName() {
        return propertyName;
    }
    public Optional<Value> getValue() {
        return Optional.ofNullable(value);
    }
}
