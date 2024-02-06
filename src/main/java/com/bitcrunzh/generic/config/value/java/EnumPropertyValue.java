package com.bitcrunzh.generic.config.value.java;

public class EnumPropertyValue<T extends Enum<T>> extends SimplePropertyValue<T> {
    public EnumPropertyValue(String propertyName, T value) {
        super(propertyName, value);
    }
}
