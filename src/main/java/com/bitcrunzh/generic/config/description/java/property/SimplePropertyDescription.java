package com.bitcrunzh.generic.config.description.java.property;

import com.bitcrunzh.generic.config.description.java.Version;
import com.bitcrunzh.generic.config.value.java.SimpleValue;
import com.bitcrunzh.generic.config.value.java.Value;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class SimplePropertyDescription<C, T> extends PropertyDescriptionBase<C, T> {
    public SimplePropertyDescription(String propertyName, String fieldName, String description, T defaultValue, Class<C> parentType, Class<T> type, PropertyValidator<T> validator, boolean isOptional, Version introducedInVersion, Function<C, T> getterFunction) {
        super(propertyName, fieldName, description, defaultValue, parentType, type, validator, isOptional, introducedInVersion, getterFunction);
    }

    @SuppressWarnings("unchecked")
    public SimplePropertyDescription(String propertyName, String fieldName, String description, T defaultValue, Class<C> parentType, Class<?> type, PropertyValidator<T> validator, boolean isOptional, Version introducedInVersion, Function<C, T> getterFunction, BiConsumer<C, T> setterFunction) {
        super(propertyName, fieldName, description, defaultValue, parentType, (Class<T>) type, validator, isOptional, introducedInVersion, getterFunction, setterFunction);
    }

    @Override
    protected T createPropertyNoValidation(Value normalizedValue) {
        SimpleValue<T> value = getValueAsType(normalizedValue, SimpleValue.class);
        return value.getValue();
    }

    @Override
    protected Value createNormalizedPropertyNoValidation(T property) {
        return new SimpleValue<>(property);
    }
}
