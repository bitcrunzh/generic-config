package com.bitcrunzh.generic.config.value;

import java.util.Optional;

public class PropertyValue<T> {
    private final String propertyName;
    private final T propertyValue;

    public PropertyValue(String propertyName, T propertyValue) {
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Optional<T> getValue() {
        return Optional.ofNullable(propertyValue);
    }
}
