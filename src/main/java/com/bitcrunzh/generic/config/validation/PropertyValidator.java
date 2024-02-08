package com.bitcrunzh.generic.config.validation;

import java.util.Optional;

public interface PropertyValidator<T> {
    ValidationResult<T> validate(T propertyValue);
}
