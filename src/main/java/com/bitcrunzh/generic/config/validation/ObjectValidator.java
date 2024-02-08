package com.bitcrunzh.generic.config.validation;

import java.util.Optional;

public interface ObjectValidator<T> {
    ValidationResult<T> validate(T object);
}
