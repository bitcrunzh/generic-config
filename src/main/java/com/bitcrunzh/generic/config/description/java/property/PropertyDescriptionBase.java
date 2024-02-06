package com.bitcrunzh.generic.config.description.java.property;

import com.bitcrunzh.generic.config.description.java.PropertyDescription;

import java.util.Optional;

public abstract class PropertyDescriptionBase<T> implements PropertyDescription<T> {
    private final String name;
    private final String description;
    private final T defaultValue;
    private final Class<T> type;

    protected PropertyDescriptionBase(String name, String description, T defaultValue, Class<T> type) {
        this.name = name;
        this.description = description;
        this.defaultValue = defaultValue;
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Optional<T> getDefaultValue() {
        return Optional.ofNullable(defaultValue);
    }

    @Override
    public Class<T> getType() {
        return type;
    }
}
