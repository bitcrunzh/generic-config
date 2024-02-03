package com.bitcrunzh.generic.config.validation;

import java.util.Optional;

public interface PropertyValidator<T> {
    Optional<PropertyProblem> validate(T propertyValue);
}
