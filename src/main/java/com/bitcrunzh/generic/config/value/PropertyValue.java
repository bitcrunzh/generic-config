package com.bitcrunzh.generic.config.value;

public interface PropertyValue<T> {
    String getName();
    T getValue();
}
