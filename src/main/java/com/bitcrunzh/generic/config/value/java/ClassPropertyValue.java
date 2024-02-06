package com.bitcrunzh.generic.config.value.java;

public class ClassPropertyValue<T> implements PropertyValue<ClassValue<T>> {
    private final String propertyName;
    private final ClassValue<T> classValue;

    public ClassPropertyValue(String propertyName, ClassValue<T> classValue) {
        this.propertyName = propertyName;
        this.classValue = classValue;
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public ClassValue<T> getValue() {
        return classValue;
    }
}
