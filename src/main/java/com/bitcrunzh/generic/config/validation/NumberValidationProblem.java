package com.bitcrunzh.generic.config.validation;

import java.util.Set;

public class NumberValidationProblem<T extends Number> implements ValidationProblem {
    private final String description;

    private NumberValidationProblem(String description) {
        this.description = description;
    }

    @Override
    public ProblemSeverity getSeverity() {
        return ProblemSeverity.ERROR;
    }

    @Override
    public String getDescription() {
        return null;
    }

    public static <T extends Number> NumberValidationProblem<T> numberTooSmall(Class<?> parentType, String fieldName, Class<?> propertyType, T value, T minValue) {
        return new NumberValidationProblem<>(String.format("Property '%s.%s:%s' value '%s' is smaller than the allowed minValue of '%s'", parentType.getSimpleName(), fieldName, propertyType.getSimpleName(), value, minValue));
    }

    public static <T extends Number> NumberValidationProblem<T> numberTooLarge(Class<?> parentType, String fieldName, Class<?> propertyType, T value, T maxValue) {
        return new NumberValidationProblem<>(String.format("Property '%s.%s:%s' value '%s' is larger than the allowed maxValue of '%s'", parentType.getSimpleName(), fieldName, propertyType.getSimpleName(), value, maxValue));
    }

    public static <T extends Number> NumberValidationProblem<T> numberNotValid(Class<?> parentType, String fieldName, Class<?> propertyType, T value, Set<T> validValues) {
        return new NumberValidationProblem<>(String.format("Property '%s.%s:%s' value '%s' is invalid. Allowed values are '%s'", parentType.getSimpleName(), fieldName, propertyType.getSimpleName(), value, validValues));
    }
}
