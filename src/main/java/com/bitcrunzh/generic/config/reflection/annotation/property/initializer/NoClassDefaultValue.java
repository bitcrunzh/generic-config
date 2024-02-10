package com.bitcrunzh.generic.config.reflection.annotation.property.initializer;

public class NoClassDefaultValue<T> implements ClassDefaultValueInitializer<T> {
    @Override
    public T getDefaultValue() {
        return null;
    }
}
