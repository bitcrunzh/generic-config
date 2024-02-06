package com.bitcrunzh.generic.config.value.java;

import java.util.Set;

public class SetPropertyValue<T> implements PropertyValue<Set<T>> {
    private final String propertyName;
    private final Set<T> value;
    private final Class<T> setEntityType;

    public SetPropertyValue(String propertyName, Class<T> setEntityType, Set<T> value) {
        this.propertyName = propertyName;
        this.value = value;
        this.setEntityType = setEntityType;
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public Set<T> getValue() {
        return value;
    }

    public Class<T> getSetEntityType() {
        return setEntityType;
    }
}
