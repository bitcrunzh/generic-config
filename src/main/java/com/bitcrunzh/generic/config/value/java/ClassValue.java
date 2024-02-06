package com.bitcrunzh.generic.config.value.java;

import java.util.Collection;

public class ClassValue<T> {
    private final Class<T> type;
    private final Collection<PropertyValue<?>> properties;

    public ClassValue(Class<T> type, Collection<PropertyValue<?>> properties) {
        this.type = type;
        this.properties = properties;
    }

    public Class<T> getType() {
        return type;
    }

    public Collection<PropertyValue<?>> getProperties() {
        return properties;
    }
}
