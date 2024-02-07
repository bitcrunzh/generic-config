package com.bitcrunzh.generic.config.description.java.property;


import com.bitcrunzh.generic.config.description.java.ClassDescription;
import com.bitcrunzh.generic.config.description.java.ClassDescriptionCache;
import com.bitcrunzh.generic.config.description.java.Version;
import com.bitcrunzh.generic.config.validation.*;
import com.bitcrunzh.generic.config.value.java.*;

import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ClassPropertyDescription<C, T> extends PropertyDescriptionBase<C, T> {
    private final Set<Class<? extends T>> subTypes;

    public ClassPropertyDescription(String propertyName, String description, T defaultValue, Class<C> parentType, Class<T> type, ClassDescriptionCache classDescriptionCache, boolean isOptional, Version introducedInVersion, Function<C, T> getterFunction, Set<Class<? extends T>> subTypes) {
        this(
                propertyName,
                description,
                defaultValue,
                parentType,
                type,
                 classDescriptionCache,
                isOptional,
                introducedInVersion,
                getterFunction,
                null,
                subTypes);
    }

    public ClassPropertyDescription(String propertyName, String description, T defaultValue, Class<C> parentType, Class<T> type, ClassDescriptionCache classDescriptionCache, boolean isOptional, Version introducedInVersion, Function<C, T> getterFunction, BiConsumer<C, T> setterFunction, Set<Class<? extends T>> subTypes) {
        super(
                propertyName,
                description,
                defaultValue,
                parentType,
                type,
                propertyValue -> {
                    @SuppressWarnings("unchecked") ClassDescription<T> classDescription = (ClassDescription<T>) classDescriptionCache.getClassDescription(propertyValue.getClass());
                    if(classDescription == null) {
                        return Optional.of(new UnknownClassDescriptionProblem(parentType, type, propertyName));
                    }
                    ValidationResult<T> validationResult = classDescription.validate(propertyValue);
                    if(validationResult.isValid()) {
                        return Optional.empty();
                    } else {
                        return Optional.of(new ValidationResultPropertyProblem<>(propertyName, validationResult));
                    }
                },
                isOptional,
                introducedInVersion,
                getterFunction,
                setterFunction);
        this.subTypes = subTypes;
    }

    @Override
    public NormalizedProperty<T> createPropertyValue(T property, ClassDescriptionCache classDescriptionCache) {
        Optional<PropertyProblem> problem = validateValue(property);
        if (problem.isPresent()) {
            throw new IllegalArgumentException(String.format("Cannot create PropertyValue as property '%s.%s:%s' is not valid. Reason: '%s'", getParentType().getSimpleName(), getPropertyName(), getType().getSimpleName(), problem.get().getDescription()));
        }
        if (property == null) {
            return new NormalizedProperty<>(getPropertyName(), null);
        }
        @SuppressWarnings("unchecked") ClassDescription<T> classDescription = (ClassDescription<T>) classDescriptionCache.getClassDescription(property.getClass());
        if (classDescription == null) {
            throw new IllegalArgumentException(String.format("ClassPropertyDescription for topType '%s', subTypes '%s', cannot be used for type '%s' as no ClassDescription could be found in the ClassDescriptionCache.", getType().getSimpleName(), printSimpleClassNames(subTypes), property.getClass().getSimpleName()));
        }
        return new NormalizedProperty<>(getPropertyName(), new ObjectValue<T>(classDescription.normalize(property, classDescriptionCache)));
    }

    @Override
    public Optional<T> createProperty(NormalizedProperty<T> normalizedProperty, ClassDescriptionCache classDescriptionCache) {
        Optional<PropertyProblem> problem = validateNormalizedProperty(normalizedProperty, classDescriptionCache);
        if (problem.isPresent()) {
            throw new IllegalArgumentException(String.format("Cannot create property '%s.%s:%s' from NormalizedProperty, as it is not valid. Reason: '%s'", getParentType().getSimpleName(), getPropertyName(), getType().getSimpleName(), problem.get().getDescription()));
        }
        if (!normalizedProperty.getValue().isPresent()) {
            return Optional.empty();
        }
        ObjectValue<T> normalizedValue = getValue(normalizedProperty.getValue().get(), ObjectValue.class);
        ClassDescription<T> classDescription = classDescriptionCache.getClassDescription(normalizedValue.getValue().getType());
        T object = classDescription.denormalize(normalizedValue.getValue(), classDescriptionCache);
        return Optional.of(object);
    }

    @Override
    public Optional<PropertyProblem> validateNormalizedProperty(NormalizedProperty<T> normalizedProperty, ClassDescriptionCache classDescriptionCache) {
        if (!normalizedProperty.getValue().isPresent()) {
            if (isOptional()) {
                return Optional.empty();
            } else {
                return Optional.of(new PropertyOptionalProblem(getPropertyName(), getType()));
            }
        }
        ObjectValue<T> objectValue = getValue(normalizedProperty.getValue().get(), ObjectValue.class);
        ClassDescription<T> classDescription = classDescriptionCache.getClassDescription(objectValue.getValue().getType());
        ValidationResult<T> validationResult = classDescription.validate(objectValue.getValue(), classDescriptionCache);
        return Optional.of(new ValidationResultPropertyProblem<>(getPropertyName(), validationResult));
    }

    private String printSimpleClassNames(Set<Class<? extends T>> subTypes) {
        if (subTypes == null || subTypes.isEmpty()) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Class<?> type : subTypes) {
            stringBuilder.append(type.getSimpleName()).append(", ");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 2);
    }
}
