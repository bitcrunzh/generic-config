package com.bitcrunzh.generic.config.value.java;

import com.bitcrunzh.generic.config.description.java.Version;

import java.util.Collection;

public class NormalizedObject<T> {
    private final Class<T> type;
    private final Collection<NormalizedProperty<?>> properties;
    private final Version modelVersion;

    public NormalizedObject(Class<T> type, Collection<NormalizedProperty<?>> properties, Version modelVersion) {
        this.type = type;
        this.properties = properties;
        this.modelVersion = modelVersion;
    }

    public Class<T> getType() {
        return type;
    }

    public Collection<NormalizedProperty<?>> getProperties() {
        return properties;
    }

    public Version getModelVersion() {
        return modelVersion;
    }
}
