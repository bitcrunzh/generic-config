package com.bitcrunzh.generic.config.description.java.property;

import com.bitcrunzh.generic.config.description.java.ClassDescriptionCache;
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
    private final String description;
    private final T defaultValue;
    private final Class<C> parentType;
    private final Class<T> type;
    private final PropertyValidator<T> validator;
    private final boolean isOptional;
    private final Version introducedInVersion;
    private final Function<C, T> getterFunction;
    private final BiConsumer<C, T> setterFunction;

    public PropertyDescriptionBase(String propertyName, String description, T defaultValue, Class<C> parentType, Class<T> type, PropertyValidator<T> validator, boolean isOptional, Version introducedInVersion, Function<C, T> getterFunction) {
        this(propertyName, description, defaultValue, parentType, type, validator, isOptional, introducedInVersion, getterFunction, null);
    }

    public PropertyDescriptionBase(String propertyName, String description, T defaultValue, Class<C> parentType, Class<T> type, PropertyValidator<T> validator, boolean isOptional, Version introducedInVersion, Function<C, T> getterFunction, BiConsumer<C, T> setterFunction) {
        this.propertyName = propertyName;
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
    public String getPropertyName() {
        return propertyName;
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
    public NormalizedProperty<T> createPropertyValueFromParent(C parent, ClassDescriptionCache classDescriptionCache) {
        T property = getterFunction.apply(parent);
        return createPropertyValue(property, classDescriptionCache);
    }

    @Override
    public ValidationResult<T> validateValueFromParent(C parentObject) {
        T value = getterFunction.apply(parentObject);
        return validateValue(value);
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

    protected <V extends Value<T>> V getValueAsType(Value<T> value, Class<?> expectedType) {
        if (!expectedType.isInstance(value)) {
            throw new IllegalArgumentException(PropertyDescriptionProblem.createDescription(getParentType(), getType(), getPropertyName(), expectedType, value.getClass()));
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
    public NormalizedProperty<T> createPropertyValue(T property, ClassDescriptionCache classDescriptionCache) {
        ValidationResult<T> validationResult = validateValue(property);
        if (validationResult.hasErrors()) {
            throw new IllegalArgumentException(String.format("Cannot create PropertyValue as property '%s.%s:%s' is not valid. Reason: '%s'", getParentType().getSimpleName(), getPropertyName(), getType().getSimpleName(), validationResult));
        }
        if (property == null) {
            return new NormalizedProperty<>(getPropertyName(), null);
        }
        Value<T> normalizedValue = createNormalizedPropertyNoValidation(property);
        return new NormalizedProperty<>(propertyName, normalizedValue);
    }

    @Override
    public Optional<T> createProperty(NormalizedProperty<T> normalizedProperty, ClassDescriptionCache classDescriptionCache) {
        T value = null;
        if (normalizedProperty.getValue().isPresent()) {
            value = createPropertyNoValidation(normalizedProperty.getValue().get());
        }
        ValidationResult<T> validationResult = validateValue(value);
        if (!validationResult.isValid()) {
            throw new IllegalArgumentException(String.format("Cannot create property '%s.%s:%s' from NormalizedProperty, as it is not valid. Reason: '%s'", getParentType().getSimpleName(), getPropertyName(), getType().getSimpleName(), validationResult));
        }
        return Optional.of(validationResult.getValidatedObject());
    }

    @Override
    public ValidationResult<T> validateNormalizedProperty(NormalizedProperty<T> normalizedProperty, ClassDescriptionCache classDescriptionCache) {
        T value = null;
        if (normalizedProperty.getValue().isEmpty()) {
            value = createPropertyNoValidation(normalizedProperty.getValue().get());
        }
        return validateValue(value);
    }

    protected abstract T createPropertyNoValidation(Value<T> normalizedValue);

    protected abstract Value<T> createNormalizedPropertyNoValidation(T property);
}
