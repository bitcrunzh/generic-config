package com.bitcrunzh.generic.config.validation;

public class PropertyValidationError implements ValidationError{
    private final String propertyPath;
    private final String description;

    public PropertyValidationError(String propertyPath, String description) {
        this.propertyPath = propertyPath;
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public String getPropertyPath() {
        return propertyPath;
    }
}
