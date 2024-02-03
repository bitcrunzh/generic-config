package com.bitcrunzh.generic.config.validation;

import java.util.Optional;

public interface ObjectValidator<T> {
    Optional<ObjectValidationProblem> validate(T object);
}
