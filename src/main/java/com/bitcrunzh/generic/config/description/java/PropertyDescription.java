package com.bitcrunzh.generic.config.description.java;

import com.bitcrunzh.generic.config.description.java.property.PropertyResourceKeyType;
import com.bitcrunzh.generic.config.validation.ValidationResult;
import com.bitcrunzh.generic.config.value.java.NormalizedProperty;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface PropertyDescription<C, T> {
    /**
     * Key which can optionally be used for looking up localized String values for property name and description.
     * @param keyType - name or description to get key for.
     * @return the unique resource key.
     */
    String getPropertyResourceKey(PropertyResourceKeyType keyType);

    /**
     * @return the non-localized human-readable property name.
     */
    String getName();

    /**
     * @return the non-localized property description.
     */
    String getDescription();

    /**
     * @return the class fieldName.
     */
    String getFieldName();

    /**
     * @return the default value if any.
     */
    Optional<T> getDefaultValue();

    /**
     * @return the java class type of the property.
     */
    Class<T> getType();

    /**
     * @return whether this property is optional to include when creating the parent object. Not including it means using the default value or null.
     */
    boolean isOptional();

    /**
     * @return the software version which this property was introduced in. Defaults to 1.0 if not specified.
     */
    Version getIntroducedInVersion();

    /**
     * Normalize a property value.
     *
     * @param property to normalize.
     * @return a normalized version of the property.
     */
    NormalizedProperty<T> createNormalizedProperty(T property);
    NormalizedProperty<T> createNormalizedPropertyFromParent(C parent);

    Optional<T> createProperty(NormalizedProperty<T> normalizedProperty);

    ValidationResult<T> validatePropertyFromParent(C parentObject);

    ValidationResult<T> validateProperty(T property);

    ValidationResult<T> validateNormalizedProperty(NormalizedProperty<T> normalizedProperty);

    Function<C, T> getGetterFunction();

    Optional<BiConsumer<C, T>> getSetterFunction();

    Class<C> getParentType();
}
