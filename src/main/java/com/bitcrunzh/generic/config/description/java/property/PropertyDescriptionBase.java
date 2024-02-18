package com.bitcrunzh.generic.config.description.java.property;

import com.bitcrunzh.generic.config.description.java.PropertyDescription;
import com.bitcrunzh.generic.config.description.java.Version;
import com.bitcrunzh.generic.config.validation.*;
import com.bitcrunzh.generic.config.value.java.NormalizedProperty;
import com.bitcrunzh.generic.config.value.java.Value;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class PropertyDescriptionBase<C, T> implements PropertyDescription<C, T> {
    private final String propertyName;
    private final String fieldName;
    private final String description;
    private final T defaultValue;
    private final Class<C> parentType;
    private final Class<T> type;
    private final PropertyValidator<T> validator;
    private final boolean isOptional;
    private final Version introducedInVersion;
    private final Function<C, T> getterFunction;
    private final BiConsumer<C, T> setterFunction;

    public PropertyDescriptionBase(String propertyName, String fieldName, String description, T defaultValue, Class<C> parentType, Class<T> type, PropertyValidator<T> validator, boolean isOptional, Version introducedInVersion, Function<C, T> getterFunction) {
        this(propertyName, fieldName, description, defaultValue, parentType, type, validator, isOptional, introducedInVersion, getterFunction, null);
    }

    public PropertyDescriptionBase(String propertyName, String fieldName, String description, T defaultValue, Class<C> parentType, Class<T> type, PropertyValidator<T> validator, boolean isOptional, Version introducedInVersion, Function<C, T> getterFunction, BiConsumer<C, T> setterFunction) {
        this.propertyName = propertyName;
        this.fieldName = fieldName;
        this.description = description;
        this.defaultValue = defaultValue;
        this.parentType = parentType;
        this.type = type;
        this.validator = validator;
        this.isOptional = isOptional;
        this.introducedInVersion = introducedInVersion;
        this.getterFunction = getterFunction;
        this.setterFunction = setterFunction;
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
        return propertyName;
    }

    @Override
    public String getFieldName() {
        return fieldName;
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

    /**
     * Defines whether it is optional to specify a value to this property.
     * Please notice that all properties added after initial version of the referencing ClassDescriptor is implicitly optional, as older version of the ClassDescriptor may not know about it.
     *
     * @return whether it is optional to specify a value to this property.
     */
    @Override
    public boolean isOptional() {
        return isOptional;
    }

    @Override
    public Version getIntroducedInVersion() {
        return introducedInVersion;
    }

    @Override
    public NormalizedProperty<T> createNormalizedPropertyFromParent(C parent) {
        T property = getterFunction.apply(parent);
        return createNormalizedProperty(property);
    }

    @Override
    public ValidationResult<T> validatePropertyFromParent(C parentObject) {
        T value = getterFunction.apply(parentObject);
        return validateProperty(value);
    }

    @Override
    public ValidationResult<T> validateProperty(T property) {
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

    protected <V extends Value> V getValueAsType(Value value, Class<?> expectedType) {
        if (!expectedType.isInstance(value)) {
            throw new IllegalArgumentException(PropertyDescriptionProblem.createDescription(getParentType(), getType(), getName(), expectedType, value.getClass()));
        }
        //noinspection unchecked
        return (V) expectedType.cast(value);
    }

    @Override
    public Function<C, T> getGetterFunction() {
        return getterFunction;
    }

    @Override
    public Optional<BiConsumer<C, T>> getSetterFunction() {
        return Optional.ofNullable(setterFunction);
    }

    @Override
    public Class<C> getParentType() {
        return parentType;
    }

    @Override
    public NormalizedProperty<T> createNormalizedProperty(T property) {
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
    public Optional<T> createProperty(NormalizedProperty<T> normalizedProperty) {
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
    public ValidationResult<T> validateNormalizedProperty(NormalizedProperty<T> normalizedProperty) {
        T value = null;
        if (!normalizedProperty.getValue().isPresent()) {
            value = createPropertyNoValidation(normalizedProperty.getValue().get());
        }
        return validateProperty(value);
    }

    protected abstract T createPropertyNoValidation(Value normalizedValue);

    protected abstract Value createNormalizedPropertyNoValidation(T property);
}
