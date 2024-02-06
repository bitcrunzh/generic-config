package com.bitcrunzh.generic.config.description.java.property;

import com.bitcrunzh.generic.config.description.java.PropertyDescription;
import com.bitcrunzh.generic.config.validation.PropertyValidationError;

import java.util.Optional;

public class ClassPropertyDescription<T> extends PropertyDescriptionBase<T> {
    public ClassPropertyDescription(String name, String description, T defaultValue, Class<T> type) {
        super(name, description, defaultValue, type);
    }

    @Override
    public PropertyValidationError validate(T value) {
        return null;
    }
}
