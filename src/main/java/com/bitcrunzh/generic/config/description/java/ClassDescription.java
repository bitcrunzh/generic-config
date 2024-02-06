package com.bitcrunzh.generic.config.description.java;

import com.bitcrunzh.generic.config.validation.*;
import com.bitcrunzh.generic.config.value.java.ObjectValue;
import com.bitcrunzh.generic.config.value.java.PropertyValue;

import java.util.*;
import java.util.function.Function;

public class ClassDescription<T> {
    private final Map<String, PropertyDescription<T, ?>> namePropertyMap = new HashMap<>();
    private final Set<String> mandatoryProperties = new HashSet<>();
    private final Class<T> type;
    private final Version modelVersion;
    private final List<PropertyDescription<T, ?>> propertyDescriptions;
    private final Function<ObjectValue<T>, T> constructorFunction;
    private final ObjectValidator<T> objectValidator;

    public ClassDescription(Class<T> type, Version modelVersion, List<PropertyDescription<T, ?>> propertyDescriptions, Function<ObjectValue<T>, T> constructorFunction, ObjectValidator<T> objectValidator) {
        this.type = type;
        this.modelVersion = modelVersion;
        this.propertyDescriptions = propertyDescriptions;
        this.constructorFunction = constructorFunction;
        this.objectValidator = objectValidator;
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

    public T create(ObjectValue<T> object) {
        ValidationResult<T> validationResult = validate(object);
        if (validationResult.isValid()) {
            throw new IllegalArgumentException(String.format("ObjectValue could not be used to create an instance of '%s' as it was not valid. Reason: '%s'", type.getSimpleName(), validationResult));
        }
        if(validationResult.getValidatedObject() != null) {
            return validationResult.getValidatedObject();
        }
        return constructorFunction.apply(object);
    }

    public ValidationResult<T> validate(T object) {
        List<ValidationProblem> validationProblems = new ArrayList<>();
        for(PropertyDescription<T, ?> propertyDescription : propertyDescriptions) {
            propertyDescription.validateFromParent(object).ifPresent(validationProblems::add);
        }
        objectValidator.validate(object).ifPresent(validationProblems::add);
        return new ValidationResult<>(object, validationProblems);
    }

    public ValidationResult<T> validate(ObjectValue<T> objectValue) {
        List<ValidationProblem> validationProblems = new ArrayList<>();
        Set<String> mandatoryPropertiesToFind = new HashSet<>(mandatoryProperties);
        for (PropertyValue<?> propertyValue : objectValue.getProperties()) {
            assertPropertyValueValid(propertyValue).ifPresent(validationProblems::add);
            mandatoryPropertiesToFind.remove(propertyValue.getPropertyName());
        }
        if (!mandatoryPropertiesToFind.isEmpty()) {
            validationProblems.add(new ObjectValidationProblem(ProblemSeverity.ERROR, String.format("ObjectValue for '%s' is missing the following mandatory PropertyValues '%s'", type.getSimpleName(), mandatoryPropertiesToFind)));
        }
        T object = constructorFunction.apply(objectValue);
        objectValidator.validate(object).ifPresent(validationProblems::add);
        return new ValidationResult<>(object, validationProblems);
    }

    private <V> Optional<PropertyProblem> assertPropertyValueValid(PropertyValue<V> propertyValue) {
        @SuppressWarnings("unchecked") PropertyDescription<T, V> propertyDescription = (PropertyDescription<T, V>) namePropertyMap.get(propertyValue.getPropertyName());
        if (propertyDescription == null) {
            return Optional.of(new UnknownPropertyWarning(propertyValue.getPropertyName(), propertyValue.getValue()));
        }
        if (propertyValue.getValue().isEmpty() && !propertyDescription.isOptional()) {
            return Optional.of(new PropertyOptionalProblem(propertyDescription.getName(), propertyDescription.getType()));
        }
        if (propertyValue.getValue().isPresent()) {
            if (!propertyDescription.getType().isInstance(propertyValue.getValue())) {
                return Optional.of(new PropertyTypeProblem(propertyDescription.getName(), propertyDescription.getType(), propertyValue.getValue().getClass()));
            }
        }
        return propertyDescription.getValidator().validate(propertyValue.getValue().orElse(null));
    }
}
