package com.bitcrunzh.generic.config.value.properties;

import com.bitcrunzh.generic.config.description.java.ClassDescription;
import com.bitcrunzh.generic.config.value.java.SimpleValue;
import com.bitcrunzh.generic.config.value.java.Value;

import java.util.Collections;
import java.util.Map;

public class PropertyValueFactory {
    public static <T> Map<String, String> createProperties(String keyPrefix, SimpleValue<T> value) {
        return Collections.emptyMap();
    }
    public static <T> Map<String, String> createProperties(String keyPrefix, Value<T> value, ClassDescription<T> classDescription) {
        return Collections.emptyMap();
    }
}
