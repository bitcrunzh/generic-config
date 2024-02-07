package com.bitcrunzh.generic.config.description.java.property;

import com.bitcrunzh.generic.config.description.java.ClassDescriptionCache;
import com.bitcrunzh.generic.config.description.java.Version;
import com.bitcrunzh.generic.config.validation.PropertyProblem;
import com.bitcrunzh.generic.config.validation.PropertyValidator;
import com.bitcrunzh.generic.config.value.java.NormalizedProperty;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class SetPropertyDescription<C, V> extends PropertyDescriptionBase<C, Set<V>> {
    //TODO implement
    public SetPropertyDescription(String propertyName, String description, Set<V> defaultValue, Class<C> parentType, Class<Set<V>> type, PropertyValidator<Set<V>> validator, boolean isOptional, Version introducedInVersion, Function<C, Set<V>> getterFunction) {
        super(propertyName, description, defaultValue, parentType, type, validator, isOptional, introducedInVersion, getterFunction);
    }

    public SetPropertyDescription(String propertyName, String description, Set<V> defaultValue, Class<C> parentType, Class<Set<V>> type, PropertyValidator<Set<V>> validator, boolean isOptional, Version introducedInVersion, Function<C, Set<V>> getterFunction, BiConsumer<C, Set<V>> setterFunction) {
        super(propertyName, description, defaultValue, parentType, type, validator, isOptional, introducedInVersion, getterFunction, setterFunction);
    }

    @Override
    public NormalizedProperty<Set<V>> createPropertyValue(Set<V> property, ClassDescriptionCache classDescriptionCache) {
        return null;
    }

    @Override
    public Optional<Set<V>> createProperty(NormalizedProperty<Set<V>> normalizedProperty, ClassDescriptionCache classDescriptionCache) {
        return Optional.empty();
    }

    @Override
    public Optional<PropertyProblem> validateNormalizedProperty(NormalizedProperty<Set<V>> normalizedProperty, ClassDescriptionCache classDescriptionCache) {
        return Optional.empty();
    }
}
