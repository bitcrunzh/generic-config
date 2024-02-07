package com.bitcrunzh.generic.config.description.java.property;

import com.bitcrunzh.generic.config.description.java.ClassDescriptionCache;
import com.bitcrunzh.generic.config.description.java.Version;
import com.bitcrunzh.generic.config.validation.PropertyDescriptionProblem;
import com.bitcrunzh.generic.config.validation.PropertyProblem;
import com.bitcrunzh.generic.config.validation.PropertyValidator;
import com.bitcrunzh.generic.config.value.java.NormalizedProperty;
import com.bitcrunzh.generic.config.value.java.SimpleValue;
import com.bitcrunzh.generic.config.value.java.Value;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class SimplePropertyDescription<C, T> extends PropertyDescriptionBase<C, T> {
    public SimplePropertyDescription(String propertyName, String description, T defaultValue, Class<C> parentType, Class<T> type, PropertyValidator<T> validator, boolean isOptional, Version introducedInVersion, Function<C, T> getterFunction) {
        super(propertyName, description, defaultValue, parentType, type, validator, isOptional, introducedInVersion, getterFunction);
    }

    public SimplePropertyDescription(String propertyName, String description, T defaultValue, Class<C> parentType, Class<T> type, PropertyValidator<T> validator, boolean isOptional, Version introducedInVersion, Function<C, T> getterFunction, BiConsumer<C, T> setterFunction) {
        super(propertyName, description, defaultValue, parentType, type, validator, isOptional, introducedInVersion, getterFunction, setterFunction);
    }

    @Override
    public NormalizedProperty<T> createPropertyValue(T property, ClassDescriptionCache classDescriptionCache) {
        Optional<PropertyProblem> validationError = validateValue(property);
        if (validationError.isPresent()) {
            throw new IllegalArgumentException(String.format("Cannot create PropertyValue as property '%s.%s:%s' is not valid. Reason: '%s'", getParentType().getSimpleName(), getPropertyName(), getType().getSimpleName(), validationError.get().getDescription()));
        }
        return new NormalizedProperty<>(getPropertyName(), new SimpleValue<>(property));
    }

    @Override
    public Optional<T> createProperty(NormalizedProperty<T> normalizedProperty, ClassDescriptionCache classDescriptionCache) {
        if(!normalizedProperty.getValue().isPresent()) {
            return Optional.empty();
        }
        SimpleValue<T> value = getValue(normalizedProperty.getValue().get(), SimpleValue.class);
        Optional<PropertyProblem> validationError = validateValue(value.getValue());
        if (validationError.isPresent()) {
            throw new IllegalArgumentException(String.format("Cannot get property value as property '%s.%s:%s' is not valid. Reason: '%s'", getParentType().getSimpleName(), validationError.get().getPropertyName(), getType().getSimpleName(), validationError.get().getDescription()));
        }
        return Optional.of(value.getValue());
    }

    @Override
    public Optional<PropertyProblem> validateNormalizedProperty(NormalizedProperty<T> normalizedProperty, ClassDescriptionCache classDescriptionCache) {
        if (normalizedProperty.getValue().isPresent()) {
            Value<T> valueWrapper = normalizedProperty.getValue().get();
            if(!(valueWrapper instanceof SimpleValue)) {
                return Optional.of(new PropertyDescriptionProblem(getParentType(), getType(), getPropertyName(), SimpleValue.class, valueWrapper.getClass()));
            }
            return validateValue(((SimpleValue<T>)valueWrapper).getValue());
        }
        return validateValue(null);
    }
}
