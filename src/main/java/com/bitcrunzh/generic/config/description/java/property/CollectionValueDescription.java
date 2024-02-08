package com.bitcrunzh.generic.config.description.java.property;

import com.bitcrunzh.generic.config.description.java.ClassDescriptionCache;
import com.bitcrunzh.generic.config.description.java.Version;
import com.bitcrunzh.generic.config.validation.PropertyProblem;
import com.bitcrunzh.generic.config.validation.ValidationResult;
import com.bitcrunzh.generic.config.value.java.NormalizedProperty;
import com.bitcrunzh.generic.config.value.java.Value;

import java.util.Optional;

public interface CollectionValueDescription<T> {
    String getDescription();

    Optional<T> getDefaultValue();

    Class<T> getType();

    Version getIntroducedInVersion();

    Value<T> createNormalizedValue(T value, ClassDescriptionCache classDescriptionCache);

    T createValue(Value<T> normalizedValue, ClassDescriptionCache classDescriptionCache);

    ValidationResult<T> validateValue(T property);

    ValidationResult<T> validateNormalizedValue(Value<T> normalizedProperty, ClassDescriptionCache classDescriptionCache);
}
