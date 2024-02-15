package com.bitcrunzh.generic.config.value.java;

import com.bitcrunzh.generic.config.description.java.Version;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class NormalizedObject<T> implements Value {
    private final Class<T> type;
    private final Map<String, NormalizedProperty<?>> propertyMap = new HashMap<>();
    private final Version modelVersion;

    public NormalizedObject(Class<T> type, Collection<NormalizedProperty<?>> properties, Version modelVersion) {
        this.type = type;
        for(NormalizedProperty<?> property : properties) {
            propertyMap.put(property.getPropertyName(), property);
        }
        this.modelVersion = modelVersion;
    }

    public Class<T> getType() {
        return type;
    }

    public Collection<NormalizedProperty<?>> getProperties() {
        return propertyMap.values();
    }

    @SuppressWarnings("unchecked")
    public <V> NormalizedProperty<V> getProperty(String propertyName) {
        return (NormalizedProperty<V>) propertyMap.get(propertyName);
    }

    public Version getModelVersion() {
        return modelVersion;
    }
}
