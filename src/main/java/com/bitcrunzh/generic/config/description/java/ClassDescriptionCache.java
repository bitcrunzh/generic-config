package com.bitcrunzh.generic.config.description.java;

import java.util.HashMap;
import java.util.Map;

public class ClassDescriptionCache {
    private final Map<Class<?>, ClassDescription<?>> typeClassDescription = new HashMap<>();

    public void addClassDescription(ClassDescription<?> classDescription) {
        typeClassDescription.put(classDescription.getType(), classDescription);
    }

    public <T> ClassDescription<T> getClassDescription(Class<T> type) {
        ClassDescription<?> classDescription = typeClassDescription.get(type);
        if (classDescription != null) {
            //noinspection unchecked
            return (ClassDescription<T>) classDescription;
        }
        return null;
    }
}
