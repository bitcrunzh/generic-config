package com.bitcrunzh.generic.config.reflection.annotation.property.initializer;

import java.util.Collections;
import java.util.Map;

public class NoMapDefaultValue<K, V> implements MapDefaultValueInitializer<K, V> {
    @Override
    public Map<K, V> getDefaultValues() {
        return Collections.emptyMap();
    }
}
