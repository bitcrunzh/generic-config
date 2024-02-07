package com.bitcrunzh.generic.config.description.java.property;

import com.bitcrunzh.generic.config.description.java.ClassDescriptionCache;
import com.bitcrunzh.generic.config.description.java.PropertyDescription;
import com.bitcrunzh.generic.config.description.java.Version;
import com.bitcrunzh.generic.config.validation.PropertyProblem;
import com.bitcrunzh.generic.config.validation.PropertyValidator;
import com.bitcrunzh.generic.config.value.java.ListValue;
import com.bitcrunzh.generic.config.value.java.NormalizedProperty;
import com.bitcrunzh.generic.config.value.java.NormalizedValueFactory;
import com.bitcrunzh.generic.config.value.java.Value;

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
        super(propertyName, description, defaultValue, parentType, createListClass(), new PropertyValidator<List<V>>() {
            @Override
            public Optional<PropertyProblem> validate(List<V> propertyValue) {
                List<PropertyProblem> problems = new ArrayList<>();
                for(V value : propertyValue) {
                    collectionValueDescription.validateValue(value).ifPresent(problems::add);
                }
                if(problems.isEmpty()) {
                    return Optional.empty();
                }
                return Optional.of()
            }
        }, isOptional, introducedInVersion, getterFunction, setterFunction);
        this.collectionValueDescription = collectionValueDescription;
    }

    private static <V> Class<List<V>> createListClass() {
        return null;
    }

    @Override
    public NormalizedProperty<List<V>> createPropertyValue(List<V> listValues, ClassDescriptionCache classDescriptionCache) {
        Optional<PropertyProblem> problem = validateValue(listValues);
        if(problem.isPresent()) {
            throw new IllegalArgumentException(String.format("Cannot create PropertyValue as property '%s.%s:%s' is not valid. Reason: '%s'", getParentType().getSimpleName(), getPropertyName(), getType().getSimpleName(), problem.get().getDescription()));

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
        Optional<PropertyProblem> problem = validateNormalizedProperty(normalizedProperty, classDescriptionCache);
        if(problem.isPresent()) {
            throw new IllegalArgumentException(String.format("Cannot create property '%s.%s:%s' from NormalizedProperty, as it is not valid. Reason: '%s'", getParentType().getSimpleName(), getPropertyName(), getType().getSimpleName(), problem.get().getDescription()));
        }
        if(!normalizedProperty.getValue().isPresent()) {
            return Optional.empty();
        }
        List<V> valueList = new ArrayList<>();
        ListValue<V> normalizedList = getValue(normalizedProperty.getValue().get(), ListValue.class);
        for(Value<V> normalizedValue : normalizedList.getValue()) {
            valueList.add(NormalizedValueFactory.convertToValue(normalizedValue, classDescriptionCache));
        }
        return Optional.of(valueList);
    }

    @Override
    public Optional<PropertyProblem> validateNormalizedProperty(NormalizedProperty<List<V>> normalizedProperty, ClassDescriptionCache classDescriptionCache) {

        return Optional.empty();
    }
}
