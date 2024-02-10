package com.bitcrunzh.generic.config.validation;

public class NoObjectValidation<T> implements ObjectValidator<T> {
    @Override
    public ValidationResult<T> validate(T object) {
        return ValidationResult.empty();
    }
}
