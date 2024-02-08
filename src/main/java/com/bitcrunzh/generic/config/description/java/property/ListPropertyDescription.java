package com.bitcrunzh.generic.config.description.java.property;

import com.bitcrunzh.generic.config.description.java.ClassDescriptionCache;
import com.bitcrunzh.generic.config.description.java.PropertyDescription;
import com.bitcrunzh.generic.config.description.java.Version;
import com.bitcrunzh.generic.config.validation.PropertyProblem;
import com.bitcrunzh.generic.config.validation.PropertyValidator;
import com.bitcrunzh.generic.config.validation.ValidationResult;
import com.bitcrunzh.generic.config.value.java.ListValue;
import com.bitcrunzh.generic.config.value.java.NormalizedProperty;
import com.bitcrunzh.generic.config.value.java.NormalizedValueFactory;
import com.bitcrunzh.generic.config.value.java.Value;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ListPropertyDescription<C, V> extends PropertyDescriptionBase<C, List<V>> {
    private final CollectionValueDescription<V> collectionValueDescription;
    public ListPropertyDescription(String propertyName, String description, List<V> defaultValue, Class<C> parentType, CollectionValueDescription<V> collectionValueDescription, boolean isOptional, Version introducedInVersion, Function<C, List<V>> getterFunction) {
        this(propertyName, description, defaultValue, parentType, collectionValueDescription, isOptional, introducedInVersion, getterFunction, null);
    }

    public ListPropertyDescription(String propertyName, String description, List<V> defaultValue, Class<C> parentType, CollectionValueDescription<V> collectionValueDescription, boolean isOptional, Version introducedInVersion, Function<C, List<V>> getterFunction, BiConsumer<C, List<V>> setterFunction) {
        super(propertyName, description, defaultValue, parentType, createListClass(), propertyValue -> {
            ValidationResult<List<V>> validationResult = ValidationResult.empty();
            for(V value : propertyValue) {
                validationResult.addValidationResult(collectionValueDescription.validateValue(value));
            }
            return validationResult;
        }, isOptional, introducedInVersion, getterFunction, setterFunction);
        this.collectionValueDescription = collectionValueDescription;
    }

    @SuppressWarnings({"unchecked", "MismatchedQueryAndUpdateOfCollection"})
    private static <V> Class<List<V>> createListClass() {
        List<V> list = new ArrayList<>();
        return (Class<List<V>>) list.getClass();
    }

    @Override
    public NormalizedProperty<List<V>> createPropertyValue(List<V> listValues, ClassDescriptionCache classDescriptionCache) {
        ValidationResult<List<V>> validationResult = validateValue(listValues);
        if(validationResult.hasErrors()) {
            throw new IllegalArgumentException(String.format("Cannot create PropertyValue as property '%s.%s:%s' is not valid. Reason: '%s'", getParentType().getSimpleName(), getPropertyName(), getType().getSimpleName(), validationResult));

        }
        if(listValues == null) {
            return new NormalizedProperty<>(getPropertyName(), null);
        }
        List<Value<V>> normalizedValues = new ArrayList<>();
        for (V value : listValues) {
            Value<V> normalizedValue = NormalizedValueFactory.convertToNormalizedValue(value, classDescriptionCache);
            normalizedValues.add(normalizedValue);
        }
        return new NormalizedProperty<>(getPropertyName(), new ListValue<>(normalizedValues));
    }

    @Override
    public Optional<List<V>> createProperty(NormalizedProperty<List<V>> normalizedProperty, ClassDescriptionCache classDescriptionCache) {
        ValidationResult<List<V>> validationResult = validateNormalizedProperty(normalizedProperty, classDescriptionCache);
        if(validationResult.hasErrors()) {
            throw new IllegalArgumentException(String.format("Cannot create property '%s.%s:%s' from NormalizedProperty, as it is not valid. Reason: '%s'", getParentType().getSimpleName(), getPropertyName(), getType().getSimpleName(), validationResult));
        }
        if(!normalizedProperty.getValue().isPresent()) {
            return Optional.empty();
        }
        List<V> valueList = new ArrayList<>();
        ListValue<V> normalizedList = getValue(normalizedProperty.getValue().get(), ListValue.class);
        for(Value<V> normalizedValue : normalizedList.getValue()) {
            ValidationResult<V> itemValidationResult = collectionValueDescription.validateNormalizedValue(normalizedValue, classDescriptionCache);
            if(itemValidationResult.hasErrors()) {
                validationResult.addValidationResult(itemValidationResult );
            }
            valueList.add(NormalizedValueFactory.convertToValue(normalizedValue, classDescriptionCache));
        }
        return Optional.of(valueList);
    }

    @Override
    public ValidationResult<List<V>> validateNormalizedProperty(NormalizedProperty<List<V>> normalizedProperty, ClassDescriptionCache classDescriptionCache) {
        //TODO
        return ValidationResult.empty();
    }
}
