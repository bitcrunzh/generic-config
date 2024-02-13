package com.bitcrunzh.generic.config.value.java;

import java.util.Map;

public class MapValue<K, V> implements Value {
    private final Map<Value, Value> value;

    public MapValue(Map<Value, Value> value) {
        this.value = value;
    }

    public Map<Value, Value> getValue() {
        return value;
    }
}
