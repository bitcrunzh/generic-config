package com.bitcrunzh.generic.config.value;

import com.bitcrunzh.generic.config.description.java.Version;

import java.util.Collection;

public class ObjectValue<T> {
    private final Class<T> type;
    private final Collection<PropertyValue<?>> properties;
    private final Version modelVersion;

    public ObjectValue(Class<T> type, Collection<PropertyValue<?>> properties, Version modelVersion) {
        this.type = type;
        this.properties = properties;
        this.modelVersion = modelVersion;
    }

    public Class<T> getType() {
        return type;
    }

    public Collection<PropertyValue<?>> getProperties() {
        return properties;
    }

    public Version getModelVersion() {
        return modelVersion;
    }
}
