package com.bitcrunzh.generic.config.value.java;

import java.util.Map;

public class MapPropertyValue<K, V> implements PropertyValue<Map<K, V>> {
    private final String propertyName;
    private final Map<K, V> value;
    private final Class<K> mapKeyType;
    private final Class<V> mapValueType;

    public MapPropertyValue(String propertyName, Class<K> mapKeyType, Class<V> mapValueType, Map<K, V> value) {
        this.propertyName = propertyName;
        this.value = value;
        this.mapKeyType = mapKeyType;
        this.mapValueType = mapValueType;
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public Map<K, V> getValue() {
        return value;
    }

    public Class<K> getMapKeyType() {
        return mapKeyType;
    }

    public Class<V> getMapValueType() {
        return mapValueType;
    }
}
