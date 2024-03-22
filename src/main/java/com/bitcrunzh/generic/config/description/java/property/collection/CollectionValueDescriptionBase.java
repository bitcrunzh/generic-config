package com.bitcrunzh.generic.config.description.java.property.collection;

import com.bitcrunzh.generic.config.description.java.PropertyDescription;
import com.bitcrunzh.generic.config.description.java.Version;
import com.bitcrunzh.generic.config.description.java.property.PropertyResourceKeyType;
import com.bitcrunzh.generic.config.validation.*;
import com.bitcrunzh.generic.config.value.java.NormalizedProperty;
import com.bitcrunzh.generic.config.value.java.Value;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class CollectionValueDescriptionBase<T> implements CollectionValueDescription<T> {
    private final String name;
    private final String description;
    private final T defaultValue;
    private final Class<T> type;
    private final Validator<T> validator;

    public CollectionValueDescriptionBase(String name, String description, T defaultValue, Class<T> type, Validator<T> validator) {
        this.name = name;
        this.description = description;
        this.defaultValue = defaultValue;
        this.type = type;
        this.validator = validator;
    }

    @Override
    public String getPropertyResourceKey(PropertyResourceKeyType keyType) {
        return String.format("%s.%s.%s", type.getPackage().getName(), fieldName, keyType.name());
    }

    private String stripSpacesAndLineFeeds(String propertyName) {
        return propertyName.replaceAll("(\\s|\\r|\\n)", "");
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

    protected <V extends Value> V getValueAsType(Value value, Class<?> expectedType) {
        if (!expectedType.isInstance(value)) {
            throw new IllegalArgumentException(PropertyDescriptionProblem.createDescription(getParentType(), getType(), getName(), expectedType, value.getClass()));
        }
        //noinspection unchecked
        return (V) expectedType.cast(value);
    }

    @Override
    public Value createNormalizedValue(T property) {
        ValidationResult<T> validationResult = validateProperty(property);
        if (validationResult.hasErrors()) {
            throw new IllegalArgumentException(String.format("Cannot create PropertyValue as property '%s.%s:%s' is not valid. Reason: '%s'", getParentType().getSimpleName(), getName(), getType().getSimpleName(), validationResult));
        }
        if (property == null) {
            return new NormalizedProperty<>(getName(), null);
        }
        Value normalizedValue = createNormalizedPropertyNoValidation(property);
        return new NormalizedProperty<>(propertyName, normalizedValue);
    }

    @Override
    public T createValue(Value normalizedProperty) {
        T value = null;
        if (normalizedProperty.getValue().isPresent()) {
            value = createPropertyNoValidation(normalizedProperty.getValue().get());
        }
        ValidationResult<T> validationResult = validateProperty(value);
        if (!validationResult.isValid()) {
            throw new IllegalArgumentException(String.format("Cannot create property '%s.%s:%s' from NormalizedProperty, as it is not valid. Reason: '%s'", getParentType().getSimpleName(), getName(), getType().getSimpleName(), validationResult));
        }
        return Optional.of(validationResult.getValidatedObject());
    }

    @Override
    public ValidationResult<T> validateValue(T property) {
        if (property != null) {
            if (!type.isInstance(property)) {
                return new ValidationResult<>(property, (new PropertyTypeProblem(propertyName, type, property.getClass())));
            }
            return validator.validate(property);
        } else if (!isOptional) {
            return new ValidationResult<>(new PropertyOptionalProblem(propertyName, type));
        }
        return ValidationResult.empty();
    }

    @Override
    public ValidationResult<T> validateNormalizedValue(Value normalizedProperty) {
        T value = null;
        if (!normalizedProperty.getValue().isPresent()) {
            value = createPropertyNoValidation(normalizedProperty.getValue().get());
        }
        return validateProperty(value);
    }

    protected abstract T createPropertyNoValidation(Value normalizedValue);

    protected abstract Value createNormalizedPropertyNoValidation(T property);
}
