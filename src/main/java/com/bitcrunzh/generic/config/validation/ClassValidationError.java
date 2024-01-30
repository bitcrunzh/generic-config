package com.bitcrunzh.generic.config.validation;

public class ClassValidationError implements ValidationError{
    private final String description;

    public ClassValidationError(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
