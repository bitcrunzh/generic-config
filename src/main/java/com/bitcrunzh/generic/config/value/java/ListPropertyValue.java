package com.bitcrunzh.generic.config.value.java;

import java.util.List;

public class ListPropertyValue<T> implements PropertyValue<List<T>> {
    private final String propertyName;
    private final Class<T> listEntityType;
    private final List<T> value;

    public ListPropertyValue(String propertyName, Class<T> listEntityType, List<T> value) {
        this.propertyName = propertyName;
        this.listEntityType = listEntityType;
        this.value = value;
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public List<T> getValue() {
        return value;
    }

    public Class<T> getListType() {
        return listEntityType;
    }
}
