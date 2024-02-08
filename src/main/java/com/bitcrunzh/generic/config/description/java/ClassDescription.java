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
    private final ObjectValidator<T> objectValidator;

    public ClassDescription(Class<T> type, Version modelVersion, List<PropertyDescription<T, ?>> propertyDescriptions, Function<NormalizedObject<T>, T> constructorFunction, ObjectValidator<T> objectValidator) {
        this.type = type;
        this.modelVersion = modelVersion;
        this.propertyDescriptions = propertyDescriptions;
        this.constructorFunction = constructorFunction;
        this.objectValidator = objectValidator;
        for (PropertyDescription<T, ?> propertyDescription : propertyDescriptions) {
            namePropertyMap.put(propertyDescription.getPropertyName(), propertyDescription);
            if (!propertyDescription.isOptional()) {
                mandatoryProperties.add(propertyDescription.getPropertyName());
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

    public T denormalize(NormalizedObject<T> object, ClassDescriptionCache classDescriptionCache) {
        ValidationResult<T> validationResult = validate(object, classDescriptionCache);
        if (validationResult.isValid()) {
            throw new IllegalArgumentException(String.format("ObjectValue could not be used to create an instance of '%s' as it was not valid. Reason: '%s'", type.getSimpleName(), validationResult));
        }
        if (validationResult.getValidatedObject() != null) {
            return validationResult.getValidatedObject();
        }
        return constructorFunction.apply(object);
    }

    public NormalizedObject<T> normalize(T object, ClassDescriptionCache classDescriptionCache) {
        if (object == null) {
            throw new IllegalArgumentException("Cannot normalize a null object.");
        }
        ValidationResult<T> validationResult = validate(object);
        if (!validationResult.isValid()) {
            throw new IllegalArgumentException(String.format("Object of type '%s' is not valid. Reason: '%s'", object.getClass(), validationResult));
        }
        List<NormalizedProperty<?>> normalizedPropertyValues = new ArrayList<>();
        for (PropertyDescription<T, ?> propertyDescription : propertyDescriptions) {
            normalizedPropertyValues.add(propertyDescription.createPropertyValueFromParent(object, classDescriptionCache));
        }
        return new NormalizedObject<>(type, normalizedPropertyValues, modelVersion);
    }

    public ValidationResult<T> validate(T object) {
        ValidationResult<T> validationResult = objectValidator.validate(object);
        for (PropertyDescription<T, ?> propertyDescription : propertyDescriptions) {
            validationResult.addValidationResult(propertyDescription.validateValueFromParent(object));
        }
        return validationResult;
    }

    public ValidationResult<T> validate(NormalizedObject<T> normalizedObject, ClassDescriptionCache classDescriptionCache) {
        ValidationResult<?> validationProblems = new ValidationResult<>();
        if (!normalizedObject.getType().equals(type)) {
            validationProblems.addValidationProblem(new ClassTypeProblem(type, normalizedObject.getType()));
        }
        Set<String> mandatoryPropertiesToFind = new HashSet<>(mandatoryProperties);
        for (NormalizedProperty<?> normalizedProperty : normalizedObject.getProperties()) {
            validationProblems.addValidationResult(assertPropertyValueValid(normalizedProperty, classDescriptionCache));
            mandatoryPropertiesToFind.remove(normalizedProperty.getPropertyName());
        }
        if (!mandatoryPropertiesToFind.isEmpty()) {
            validationProblems.addValidationProblem(new ObjectValidationProblem(ProblemSeverity.ERROR, String.format("ObjectValue for '%s' is missing the following mandatory PropertyValues '%s'", type.getSimpleName(), mandatoryPropertiesToFind)));
        }
        T object = constructorFunction.apply(normalizedObject);
        ValidationResult<T> validationResult = objectValidator.validate(object);
        validationResult.addValidationResult(validationProblems);
        return validationResult;
    }

    private <V> ValidationResult<V> assertPropertyValueValid(NormalizedProperty<V> normalizedProperty, ClassDescriptionCache classDescriptionCache) {
        @SuppressWarnings("unchecked") PropertyDescription<T, V> propertyDescription = (PropertyDescription<T, V>) namePropertyMap.get(normalizedProperty.getPropertyName());
        if (propertyDescription == null) {
            return new ValidationResult<>(new UnknownPropertyWarning(normalizedProperty.getPropertyName(), normalizedProperty.getValue()));
        }
        return propertyDescription.validateNormalizedProperty(normalizedProperty, classDescriptionCache);
    }
}
