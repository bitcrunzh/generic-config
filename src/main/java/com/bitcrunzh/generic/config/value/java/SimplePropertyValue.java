package com.bitcrunzh.generic.config.value.java;

public abstract class SimplePropertyValue<T> implements PropertyValue<T> {
    private final String propertyName;
    private final T value;

    public SimplePropertyValue(String propertyName, T value) {
        this.propertyName = propertyName;
        this.value = value;
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public T getValue() {
        return value;
    }
}
