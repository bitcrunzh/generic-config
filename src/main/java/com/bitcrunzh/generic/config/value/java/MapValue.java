package com.bitcrunzh.generic.config.value.java;

import java.util.Map;
import java.util.Optional;

public class MapValue<K, V> implements Value<Map<K, V>> {
    private final Map<Value<K>, Value<V>> value;

    public MapValue(Map<Value<K>, Value<V>> value) {
        this.value = value;
    }

    public Map<Value<K>, Value<V>> getValue() {
        return value;
    }
}
