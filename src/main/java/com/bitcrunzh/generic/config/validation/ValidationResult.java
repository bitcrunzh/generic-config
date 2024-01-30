package com.bitcrunzh.generic.config.validation;

import java.util.List;

public class ValidationResult {
    private final List<ValidationError> errors;

    public ValidationResult(List<ValidationError> errors) {
        this.errors = errors;
    }

    public boolean isValid() {
        return errors.isEmpty();
    }
}
