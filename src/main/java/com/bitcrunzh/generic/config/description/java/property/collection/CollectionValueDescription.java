package com.bitcrunzh.generic.config.description.java.property.collection;

import com.bitcrunzh.generic.config.description.java.ClassDescriptionCache;
import com.bitcrunzh.generic.config.description.java.Version;
import com.bitcrunzh.generic.config.description.java.property.PropertyResourceKeyType;
import com.bitcrunzh.generic.config.validation.ValidationResult;
import com.bitcrunzh.generic.config.value.java.Value;

import java.util.Optional;

public interface CollectionValueDescription<T> {
    String getPropertyResourceKey(PropertyResourceKeyType keyType);
    String getName();
    String getDescription();

    Optional<T> getDefaultValue();

    Class<T> getType();

    Value createNormalizedValue(T value);

    T createValue(Value normalizedValue);

    ValidationResult<T> validateValue(T property);

    ValidationResult<T> validateNormalizedValue(Value normalizedProperty);
}
