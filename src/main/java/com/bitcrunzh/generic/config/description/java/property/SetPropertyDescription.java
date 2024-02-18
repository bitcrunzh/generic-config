package com.bitcrunzh.generic.config.description.java.property;

import com.bitcrunzh.generic.config.description.java.ClassDescriptionCache;
import com.bitcrunzh.generic.config.description.java.Version;
import com.bitcrunzh.generic.config.validation.PropertyValidator;
import com.bitcrunzh.generic.config.validation.ValidationResult;
import com.bitcrunzh.generic.config.value.java.SetValue;
import com.bitcrunzh.generic.config.value.java.Value;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class SetPropertyDescription<C, V> extends PropertyDescriptionBase<C, Set<V>> {
    private final CollectionValueDescription<V> collectionValueDescription;
    private final ClassDescriptionCache classDescriptionCache;
    public SetPropertyDescription(String propertyName, String fieldName, String description, Set<V> defaultValue, Class<C> parentType, PropertyValidator<Set<V>> validator, CollectionValueDescription<V> collectionValueDescription, boolean isOptional, Version introducedInVersion, Function<C, Set<V>> getterFunction, ClassDescriptionCache classDescriptionCache) {
        this(propertyName, fieldName, description, defaultValue, parentType, validator, collectionValueDescription, isOptional, introducedInVersion, getterFunction, null, classDescriptionCache);
    }

    public SetPropertyDescription(String propertyName, String fieldName, String description, Set<V> defaultValue, Class<C> parentType, PropertyValidator<Set<V>> validator, CollectionValueDescription<V> collectionValueDescription, boolean isOptional, Version introducedInVersion, Function<C, Set<V>> getterFunction, BiConsumer<C, Set<V>> setterFunction, ClassDescriptionCache classDescriptionCache) {
        super(propertyName, fieldName, description, defaultValue, parentType, createSetClass(), propertyValue -> {
            ValidationResult<Set<V>> validationResult = validator.validate(propertyValue);
            for (V v : propertyValue) {
                validationResult.addValidationResult(collectionValueDescription.validateValue(v));
            }
            return validationResult;
        }, isOptional, introducedInVersion, getterFunction, setterFunction);
        this.collectionValueDescription = collectionValueDescription;
        this.classDescriptionCache = classDescriptionCache;
    }

    @SuppressWarnings({"unchecked", "MismatchedQueryAndUpdateOfCollection"})
    private static <V> Class<Set<V>> createSetClass() {
        Set<V> set = new HashSet<>();
        return (Class<Set<V>>) set.getClass();
    }

    @Override
    protected Set<V> createPropertyNoValidation(Value normalizedValue) {
        if(normalizedValue == null) {
            return null;
        }
        SetValue<V> normalizedSet = getValueAsType(normalizedValue, SetValue.class);
        Set<V> valueSet = new HashSet<>();
        for(Value normalizedSetElement : normalizedSet.getValue()) {
            valueSet.add(collectionValueDescription.convertToValue(normalizedSetElement, classDescriptionCache));
        }
        return valueSet;
    }

    @Override
    protected Value createNormalizedPropertyNoValidation(Set<V> property) {
        if(property == null) {
            return null;
        }
        Set<Value> normalizedValues = new HashSet<>();
        for (V value : property) {
            Value normalizedValue = collectionValueDescription.convertToNormalizedValue(value, classDescriptionCache);
            normalizedValues.add(normalizedValue);
        }
        return new SetValue<>(normalizedValues);
    }
}
