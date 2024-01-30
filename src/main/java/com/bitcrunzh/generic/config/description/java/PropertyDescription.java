package com.bitcrunzh.generic.config.description.java;

import com.bitcrunzh.generic.config.validation.PropertyValidationError;

import java.util.Optional;

public interface PropertyDescription<T> {
    String getName();
    String getDescription();
    Optional<T> getDefaultValue();
    Class<T> getType();
    PropertyValidationError validate(T value);
}
