package com.bitcrunzh.generic.config.value.java;

public class ArrayPropertyValue<T> implements PropertyValue<T[]> {
    private final String propertyName;
    private final Class<T> arrayEntityType;
    private final T[] value;

    public ArrayPropertyValue(String propertyName, Class<T> arrayEntityType, T[] value) {
        this.propertyName = propertyName;
        this.arrayEntityType = arrayEntityType;
        this.value = value;
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public T[] getValue() {
        return value;
    }

    public Class<T> getArrayEntityType() {
        return arrayEntityType;
    }
}
