package com.bitcrunzh.generic.config.validation;

public class NoValidation<T> implements Validator<T> {
    @Override
    public ValidationResult<T> validate(T object) {
        return ValidationResult.empty();
    }
}
