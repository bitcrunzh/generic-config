package com.bitcrunzh.generic.config.description.java;

import com.bitcrunzh.generic.config.validation.ValidationResult;

import java.util.List;

public interface ClassDescription<T> {
    Class<T> getType();
    String getVersion();
    List<PropertyDescription<?>> getProperties();
    T create(ClassValue classValue);
    ValidationResult validate(T object);
}
