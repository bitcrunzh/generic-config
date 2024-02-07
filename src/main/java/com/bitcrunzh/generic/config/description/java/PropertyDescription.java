package com.bitcrunzh.generic.config.description.java;

import com.bitcrunzh.generic.config.validation.PropertyProblem;
import com.bitcrunzh.generic.config.validation.PropertyValidator;
import com.bitcrunzh.generic.config.value.java.NormalizedProperty;
import com.bitcrunzh.generic.config.value.java.Value;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface PropertyDescription<C, T> {
    String getPropertyName();

    String getDescription();

    Optional<T> getDefaultValue();

    Class<T> getType();

    boolean isOptional();

    Version getIntroducedInVersion();

    NormalizedProperty<T> createPropertyValue(T property, ClassDescriptionCache classDescriptionCache);
    NormalizedProperty<T> createPropertyValueFromParent(C parent, ClassDescriptionCache classDescriptionCache);

    Optional<T> createProperty(NormalizedProperty<T> normalizedProperty, ClassDescriptionCache classDescriptionCache);

    Optional<PropertyProblem> validateValueFromParent(C parentObject);

    Optional<PropertyProblem> validateValue(T property);

    Optional<PropertyProblem> validateNormalizedProperty(NormalizedProperty<T> normalizedProperty, ClassDescriptionCache classDescriptionCache);

    Function<C, T> getGetterFunction();

    Optional<BiConsumer<C, T>> getSetterFunction();

    Class<C> getParentType();
}
