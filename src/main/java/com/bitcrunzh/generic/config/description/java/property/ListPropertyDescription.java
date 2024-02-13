package com.bitcrunzh.generic.config.description.java.property;

import com.bitcrunzh.generic.config.description.java.ClassDescriptionCache;
import com.bitcrunzh.generic.config.description.java.Version;
import com.bitcrunzh.generic.config.validation.PropertyValidator;
import com.bitcrunzh.generic.config.validation.ValidationResult;
import com.bitcrunzh.generic.config.value.java.ListValue;
import com.bitcrunzh.generic.config.value.java.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ListPropertyDescription<C, V> extends PropertyDescriptionBase<C, List<V>> {
    private final CollectionValueDescription<V> collectionValueDescription;
    private final ClassDescriptionCache classDescriptionCache;
    public ListPropertyDescription(String propertyName, String description, List<V> defaultValue, Class<C> parentType, PropertyValidator<List<V>> validator, CollectionValueDescription<V> collectionValueDescription, boolean isOptional, Version introducedInVersion, Function<C, List<V>> getterFunction, ClassDescriptionCache classDescriptionCache) {
        this(propertyName, description, defaultValue, parentType, validator, collectionValueDescription, isOptional, introducedInVersion, getterFunction, null, classDescriptionCache);
    }

    public ListPropertyDescription(String propertyName, String description, List<V> defaultValue, Class<C> parentType, PropertyValidator<List<V>> validator, CollectionValueDescription<V> collectionValueDescription, boolean isOptional, Version introducedInVersion, Function<C, List<V>> getterFunction, BiConsumer<C, List<V>> setterFunction, ClassDescriptionCache classDescriptionCache) {
        super(propertyName, description, defaultValue, parentType, createListClass(), propertyValue -> {
            ValidationResult<List<V>> validationResult = validator.validate(propertyValue);
            for (V v : propertyValue) {
                validationResult.addValidationResult(collectionValueDescription.validateValue(v));
            }
            return validationResult;
        }, isOptional, introducedInVersion, getterFunction, setterFunction);
        this.collectionValueDescription = collectionValueDescription;
        this.classDescriptionCache = classDescriptionCache;
    }

    @SuppressWarnings({"unchecked", "MismatchedQueryAndUpdateOfCollection"})
    private static <V> Class<List<V>> createListClass() {
        List<V> list = new ArrayList<>();
        return (Class<List<V>>) list.getClass();
    }

    @Override
    protected List<V> createPropertyNoValidation(Value normalizedValue) {
        if(normalizedValue == null) {
            return null;
        }
        ListValue<V> normalizedList = getValueAsType(normalizedValue, ListValue.class);
        List<V> valueList = new ArrayList<>();
        for(Value normalizedListElement : normalizedList.getValue()) {
            valueList.add(collectionValueDescription.convertToValue(normalizedListElement, classDescriptionCache));
        }
        return valueList;
    }

    @Override
    protected Value createNormalizedPropertyNoValidation(List<V> property) {
        if(property == null) {
            return null;
        }
        List<Value> normalizedValues = new ArrayList<>();
        for (V value : property) {
            Value normalizedValue = collectionValueDescription.convertToNormalizedValue(value, classDescriptionCache);
            normalizedValues.add(normalizedValue);
        }
        return new ListValue<>(normalizedValues);
    }
}
