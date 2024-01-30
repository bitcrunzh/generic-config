package com.bitcrunzh.generic.config.description.java.property;

import com.bitcrunzh.generic.config.description.java.PropertyDescription;
import com.bitcrunzh.generic.config.validation.PropertyValidationError;

import java.util.Optional;

public class ClassPropertyDescription<T> implements PropertyDescription<T> {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public Optional<T> getDefaultValue() {
        return null;
    }

    @Override
    public Class<T> getType() {
        return null;
    }

    @Override
    public PropertyValidationError validate(T value) {
        return null;
    }
}
