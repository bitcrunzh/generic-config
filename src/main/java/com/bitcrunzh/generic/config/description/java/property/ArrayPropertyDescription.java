package com.bitcrunzh.generic.config.description.java.property;

import com.bitcrunzh.generic.config.description.java.ClassDescriptionCache;
import com.bitcrunzh.generic.config.description.java.Version;
import com.bitcrunzh.generic.config.validation.PropertyProblem;
import com.bitcrunzh.generic.config.validation.PropertyValidator;
import com.bitcrunzh.generic.config.value.java.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ArrayPropertyDescription<C, V> extends PropertyDescriptionBase<C, V[]> {
    public ArrayPropertyDescription(String propertyName, String description,V[] defaultValue, Class<C> parentType, Class<V[]> type, PropertyValidator<V[]> validator, boolean isOptional, Version introducedInVersion, Function<C, V[]> getterFunction) {
        super(propertyName, description, defaultValue, parentType, type, validator, isOptional, introducedInVersion, getterFunction);
    }

    public ArrayPropertyDescription(String propertyName, String description, V[] defaultValue, Class<C> parentType, Class<V[]> type, PropertyValidator<V[]> validator, boolean isOptional, Version introducedInVersion, Function<C, V[]> getterFunction, BiConsumer<C, V[]> setterFunction) {
        super(propertyName, description, defaultValue, parentType, type, validator, isOptional, introducedInVersion, getterFunction, setterFunction);
    }

    @Override
    public NormalizedProperty<V[]> createPropertyValue(V[] listValues, ClassDescriptionCache classDescriptionCache) {
        Optional<PropertyProblem> problem = validateValue(listValues);
        if(problem.isPresent()) {
            throw new IllegalArgumentException(String.format("Cannot create PropertyValue as property '%s.%s:%s' is not valid. Reason: '%s'", getParentType().getSimpleName(), getPropertyName(), getType().getSimpleName(), problem.get().getDescription()));

        }
        if(listValues == null) {
            return new NormalizedProperty<>(getPropertyName(), null);
        }
        @SuppressWarnings("unchecked") Value<V>[] normalizedValues = new Value[listValues.length];

        for (int i = 0; i < listValues.length; i++) {
            V value = listValues[i];
            Value<V> normalizedValue = NormalizedValueFactory.convertToNormalizedValue(value, classDescriptionCache);
            normalizedValues[i] = normalizedValue;
        }
        return new NormalizedProperty<>(getPropertyName(), new ArrayValue<>(normalizedValues));
    }

    @Override
    public Optional<V[]> createProperty(NormalizedProperty<V[]> normalizedProperty, ClassDescriptionCache classDescriptionCache) {
        Optional<PropertyProblem> problem = validateNormalizedProperty(normalizedProperty, classDescriptionCache);
        if(problem.isPresent()) {
            throw new IllegalArgumentException(String.format("Cannot create property '%s.%s:%s' from NormalizedProperty, as it is not valid. Reason: '%s'", getParentType().getSimpleName(), getPropertyName(), getType().getSimpleName(), problem.get().getDescription()));
        }
        if(!normalizedProperty.getValue().isPresent()) {
            return Optional.empty();
        }
        ArrayValue<V> normalizedList = getValue(normalizedProperty.getValue().get(), ArrayValue.class);
        List<V> valueList = new ArrayList<>();
        for(Value<V> normalizedValue : normalizedList.getValue()) {
            valueList.add(NormalizedValueFactory.convertToValue(normalizedValue, classDescriptionCache));
        }
        //TODO create a generic array...
        return Optional.of(valueList.toArray());
    }

    @Override
    public Optional<PropertyProblem> validateNormalizedProperty(NormalizedProperty<V[]> normalizedProperty, ClassDescriptionCache classDescriptionCache) {
        //TODO
        return Optional.empty();
    }
}
