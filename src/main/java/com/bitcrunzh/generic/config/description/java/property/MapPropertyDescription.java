package com.bitcrunzh.generic.config.description.java.property;

import com.bitcrunzh.generic.config.description.java.ClassDescriptionCache;
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
//TODO support map
public class MapPropertyDescription<C, V> extends PropertyDescriptionBase<C, List<V>> {
    public MapPropertyDescription(String propertyName, String description, List<V> defaultValue, Class<C> parentType, Class<List<V>> type, PropertyValidator<List<V>> validator, boolean isOptional, Version introducedInVersion, Function<C, List<V>> getterFunction) {
        super(propertyName, description, defaultValue, parentType, type, validator, isOptional, introducedInVersion, getterFunction);
    }

    public MapPropertyDescription(String propertyName, String description, List<V> defaultValue, Class<C> parentType, Class<List<V>> type, PropertyValidator<List<V>> validator, boolean isOptional, Version introducedInVersion, Function<C, List<V>> getterFunction, BiConsumer<C, List<V>> setterFunction) {
        super(propertyName, description, defaultValue, parentType, type, validator, isOptional, introducedInVersion, getterFunction, setterFunction);
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
