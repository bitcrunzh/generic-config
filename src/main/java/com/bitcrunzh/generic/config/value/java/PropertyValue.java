package com.bitcrunzh.generic.config.value.java;

public interface PropertyValue<T> {
    String getPropertyName();
    T getValue();
}
