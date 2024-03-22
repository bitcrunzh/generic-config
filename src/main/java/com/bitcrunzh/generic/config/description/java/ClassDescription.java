package com.bitcrunzh.generic.config.description.java;

import com.bitcrunzh.generic.config.validation.*;
import com.bitcrunzh.generic.config.value.java.NormalizedObject;
import com.bitcrunzh.generic.config.value.java.NormalizedProperty;

import java.util.*;
import java.util.function.Function;

public class ClassDescription<T> {
    private final Map<String, PropertyDescription<T, ?>> namePropertyMap = new HashMap<>();
    private final Set<String> mandatoryProperties = new HashSet<>();
    private final Class<T> type;
    private final Version modelVersion;
    private final List<PropertyDescription<T, ?>> propertyDescriptions;
    private final Function<NormalizedObject<T>, T> constructorFunction;
    private final Validator<T> validator;

    public ClassDescription(Class<T> type, Version modelVersion, List<PropertyDescription<T, ?>> propertyDescriptions, Function<NormalizedObject<T>, T> constructorFunction, Validator<T> validator) {
        this.type = type;
        this.modelVersion = modelVersion;
        this.propertyDescriptions = propertyDescriptions;
        this.constructorFunction = constructorFunction;
        this.validator = validator;
        for (PropertyDescription<T, ?> propertyDescription : propertyDescriptions) {
            namePropertyMap.put(propertyDescription.getName(), propertyDescription);
            if (!propertyDescription.isOptional()) {
                mandatoryProperties.add(propertyDescription.getName());
            }
        }
    }

    public Class<T> getType() {
        return type;
    }

    public Version getVersion() {
        return modelVersion;
    }

    public List<PropertyDescription<T, ?>> getProperties() {
        return Collections.unmodifiableList(propertyDescriptions);
    }

    public T denormalize(NormalizedObject<T> object) {
        ValidationResult<T> validationResult = validate(object);
        if (validationResult.isValid()) {
            throw new IllegalArgumentException(String.format("ObjectValue could not be used to create an instance of '%s' as it was not valid. Reason: '%s'", type.getSimpleName(), validationResult));
        }
        if (validationResult.getValidatedObject() != null) {
            return validationResult.getValidatedObject();
        }
        return constructorFunction.apply(object);
    }

    public NormalizedObject<T> normalize(T object) {
        if (object == null) {
            throw new IllegalArgumentException("Cannot normalize a null object.");
        }
        ValidationResult<T> validationResult = validate(object);
        if (!validationResult.isValid()) {
            throw new IllegalArgumentException(String.format("Object of type '%s' is not valid. Reason: '%s'", object.getClass(), validationResult));
        }
        List<NormalizedProperty<?>> normalizedPropertyValues = new ArrayList<>();
        for (PropertyDescription<T, ?> propertyDescription : propertyDescriptions) {
            normalizedPropertyValues.add(propertyDescription.createNormalizedPropertyFromParent(object));
        }
        return new NormalizedObject<>(type, normalizedPropertyValues, modelVersion);
    }

    public ValidationResult<T> validate(T object) {
        ValidationResult<T> validationResult = validator.validate(object);
        for (PropertyDescription<T, ?> propertyDescription : propertyDescriptions) {
            validationResult.addValidationResult(propertyDescription.validatePropertyFromParent(object));
        }
        return validationResult;
    }

    public ValidationResult<T> validate(NormalizedObject<T> normalizedObject) {
        ValidationResult<?> validationProblems = new ValidationResult<>();
        if (!normalizedObject.getType().equals(type)) {
            validationProblems.addValidationProblem(new ClassTypeProblem(type, normalizedObject.getType()));
        }
        Set<String> mandatoryPropertiesToFind = new HashSet<>(mandatoryProperties);
        for (NormalizedProperty<?> normalizedProperty : normalizedObject.getProperties()) {
            validationProblems.addValidationResult(assertPropertyValueValid(normalizedProperty));
            mandatoryPropertiesToFind.remove(normalizedProperty.getPropertyName());
        }
        if (!mandatoryPropertiesToFind.isEmpty()) {
            validationProblems.addValidationProblem(new ObjectValidationProblem(ProblemSeverity.ERROR, String.format("ObjectValue for '%s' is missing the following mandatory PropertyValues '%s'", type.getSimpleName(), mandatoryPropertiesToFind)));
        }
        T object = constructorFunction.apply(normalizedObject);
        ValidationResult<T> validationResult = validator.validate(object);
        validationResult.addValidationResult(validationProblems);
        return validationResult;
    }

    private <V> ValidationResult<V> assertPropertyValueValid(NormalizedProperty<V> normalizedProperty) {
        @SuppressWarnings("unchecked") PropertyDescription<T, V> propertyDescription = (PropertyDescription<T, V>) namePropertyMap.get(normalizedProperty.getPropertyName());
        if (propertyDescription == null) {
            return new ValidationResult<>(new UnknownPropertyWarning(normalizedProperty.getPropertyName(), normalizedProperty.getValue()));
        }
        return propertyDescription.validateNormalizedProperty(normalizedProperty);
    }
}
