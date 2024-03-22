package com.bitcrunzh.generic.config.validation;

public interface Validator<T> {
    ValidationResult<T> validate(T object);
}
