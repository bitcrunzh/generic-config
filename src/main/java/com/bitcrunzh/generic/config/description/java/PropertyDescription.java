package com.bitcrunzh.generic.config.description.java;

import com.bitcrunzh.generic.config.validation.PropertyProblem;
import com.bitcrunzh.generic.config.validation.PropertyOptionalProblem;
import com.bitcrunzh.generic.config.validation.PropertyValidator;
import com.bitcrunzh.generic.config.value.PropertyValue;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class PropertyDescription<C, T> {
    private final String name;
    private final String description;
    private final T defaultValue;
    private final Class<T> type;
    private final PropertyValidator<T> validator;
    private final boolean isOptional;
    private final Version introducedInVersion;
    private final Function<C, T> getterFunction;
    private final BiConsumer<C, T> setterFunction;

    protected PropertyDescription(String name, String description, T defaultValue, Class<T> type, PropertyValidator<T> validator, boolean isOptional, Version introducedInVersion, Function<C, T> getterFunction) {
        this(name, description, defaultValue, type, validator, isOptional, introducedInVersion, getterFunction, null);
    }

    protected PropertyDescription(String name, String description, T defaultValue, Class<T> type, PropertyValidator<T> validator, boolean isOptional, Version introducedInVersion, Function<C, T> getterFunction, BiConsumer<C, T> setterFunction) {
        this.name = name;
        this.description = description;
        this.defaultValue = defaultValue;
        this.type = type;
        this.validator = validator;
        this.isOptional = isOptional;
        this.introducedInVersion = introducedInVersion;
        this.getterFunction = getterFunction;
        this.setterFunction = setterFunction;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Optional<T> getDefaultValue() {
        return Optional.ofNullable(defaultValue);
    }

    public Class<T> getType() {
        return type;
    }

    public PropertyValidator<T> getValidator() {
        return validator;
    }

    /**
     * Defines whether it is optional to specify a value to this property.
     * Please notice that all properties added after initial version of the referencing ClassDescriptor is implicitly optional, as older version of the ClassDescriptor may not know about it.
     *
     * @return whether it is optional to specify a value to this property.
     */
    public boolean isOptional() {
        return isOptional;
    }

    public Version getIntroducedInVersion() {
        return introducedInVersion;
    }

    public PropertyValue<T> createPropertyValue(T property) {
        Optional<PropertyProblem> validationError = validator.validate(property);
        if (validationError.isPresent()) {
            throw new IllegalArgumentException(String.format("Cannot create PropertyValue as property '%s:%s' is not valid. Reason: '%s'", validationError.get().getPropertyName(), type.getSimpleName(), validationError.get().getDescription()));
        }
        return new PropertyValue<>(name, property);
    }

    public Optional<T> createProperty(PropertyValue<T> propertyValue) {
        T value = propertyValue.getValue().orElse(null);
        Optional<PropertyProblem> validationError = validator.validate(value);
        if (validationError.isPresent()) {
            throw new IllegalArgumentException(String.format("Cannot get property value as property '%s:%s' is not valid. Reason: '%s'", validationError.get().getPropertyName(), type.getSimpleName(), validationError.get().getDescription()));
        }
        return propertyValue.getValue();
    }

    public Optional<PropertyProblem> validateFromParent(C parentObject) {
        T value = getterFunction.apply(parentObject);
        return validate(value);
    }

    public Optional<PropertyProblem> validate(T property) {
        return validate(new PropertyValue<>(name, property));
    }

    public Optional<PropertyProblem> validate(PropertyValue<T> propertyValue) {
        if (propertyValue.getValue().isPresent()) {
            return validator.validate(propertyValue.getValue().orElse(null));
        } else if (!isOptional) {
            return Optional.of(new PropertyOptionalProblem(name, type));
        }
        return Optional.empty();
    }

    public Function<C, T> getGetterFunction() {
        return getterFunction;
    }

    public Optional<BiConsumer<C, T>> getSetterFunction() {
        return Optional.ofNullable(setterFunction);
    }
}
