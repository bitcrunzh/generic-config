package com.bitcrunzh.generic.config.value.java;

import java.util.Optional;

public class ObjectValue<T> implements Value<T> {
    private final NormalizedObject<T> classValue;

    public ObjectValue(NormalizedObject<T> classValue) {
        this.classValue = classValue;
    }

    public NormalizedObject<T> getValue() {
        return classValue;
    }
}
