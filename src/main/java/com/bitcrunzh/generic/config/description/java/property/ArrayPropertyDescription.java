package com.bitcrunzh.generic.config.description.java.property;

import com.bitcrunzh.generic.config.description.java.ClassDescriptionCache;
import com.bitcrunzh.generic.config.description.java.Version;
import com.bitcrunzh.generic.config.validation.PropertyValidator;
import com.bitcrunzh.generic.config.validation.ValidationResult;
import com.bitcrunzh.generic.config.value.java.*;

import java.lang.reflect.Array;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ArrayPropertyDescription<C, V> extends PropertyDescriptionBase<C, V[]> {
    private final CollectionValueDescription<V> collectionValueDescription;
    private final ClassDescriptionCache classDescriptionCache;

    public ArrayPropertyDescription(String propertyName, String description, V[] defaultValue, Class<C> parentType, PropertyValidator<V[]> validator, CollectionValueDescription<V> collectionValueDescription, boolean isOptional, Version introducedInVersion, Function<C, V[]> getterFunction, ClassDescriptionCache classDescriptionCache) {
        this(propertyName, description, defaultValue, parentType, validator, collectionValueDescription, isOptional, introducedInVersion, getterFunction, null, classDescriptionCache);
    }

    public ArrayPropertyDescription(String propertyName, String description, V[] defaultValue, Class<C> parentType, PropertyValidator<V[]> validator, CollectionValueDescription<V> collectionValueDescription, boolean isOptional, Version introducedInVersion, Function<C, V[]> getterFunction, BiConsumer<C, V[]> setterFunction, ClassDescriptionCache classDescriptionCache) {
        super(propertyName, description, defaultValue, parentType, createType(collectionValueDescription.getType()), propertyValue -> {
            ValidationResult<V[]> validationResult = validator.validate(propertyValue);
            for (V v : propertyValue) {
                validationResult.addValidationResult(collectionValueDescription.validateValue(v));
            }
            return validationResult;
        }, isOptional, introducedInVersion, getterFunction, setterFunction);
        this.collectionValueDescription = collectionValueDescription;
        this.classDescriptionCache = classDescriptionCache;
    }

    @SuppressWarnings("unchecked")
    private static <V> Class<V[]> createType(Class<V> type) {
        V[] valueArray = (V[]) Array.newInstance(type, 0);
        return (Class<V[]>) valueArray.getClass();
    }

    @Override
    public V[] createPropertyNoValidation(Value<V[]> normalizedValue) {
        if (normalizedValue == null) {
            return null;
        }
        ArrayValue<V> normalizedList = getValueAsType(normalizedValue, ArrayValue.class);
        @SuppressWarnings("unchecked") V[] valueArray = (V[]) Array.newInstance(collectionValueDescription.getType(), normalizedList.getValue().length);
        for (int i = 0; i < normalizedList.getValue().length; i++) {
            valueArray[i] = collectionValueDescription.convertToValue(normalizedList.getValue()[i], classDescriptionCache);
        }
        return valueArray;
    }

    @Override
    protected Value<V[]> createNormalizedPropertyNoValidation(V[] listValues) {
        @SuppressWarnings("unchecked") Value<V>[] normalizedValues = new Value[listValues.length];
        for (int i = 0; i < listValues.length; i++) {
            V value = listValues[i];
            Value<V> normalizedValue = collectionValueDescription.convertToNormalizedValue(value, classDescriptionCache);
            normalizedValues[i] = normalizedValue;
        }
        return new ArrayValue<>(normalizedValues);
    }
}
