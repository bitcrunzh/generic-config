package com.bitcrunzh.generic.config.validation;

public class ValidationResultPropertyProblem<T> extends PropertyProblem {
    private final ValidationResult<T> validationResult;
    public ValidationResultPropertyProblem(String propertyName, ValidationResult<T> validationResult) {
        super(validationResult.getHighestSeverity(), propertyName, validationResult.toString());
        this.validationResult = validationResult;
    }

    public ValidationResult<T> getValidationResult() {
        return validationResult;
    }
}
