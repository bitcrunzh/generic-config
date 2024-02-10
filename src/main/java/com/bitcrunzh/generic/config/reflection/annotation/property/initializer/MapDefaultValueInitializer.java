package com.bitcrunzh.generic.config.reflection.annotation.property.initializer;

import java.util.Map;

public interface MapDefaultValueInitializer<K, V> {
    Map<K, V> getDefaultValues();
}
