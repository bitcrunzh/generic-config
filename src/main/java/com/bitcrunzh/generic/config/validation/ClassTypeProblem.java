package com.bitcrunzh.generic.config.validation;

public class ClassTypeProblem extends ObjectValidationProblem {
    private final Class<?> expectedType;
    private final Class<?> actualType;

    public ClassTypeProblem(Class<?> expectedType, Class<?> actualType) {
        super(ProblemSeverity.ERROR, String.format("Expected type '%s', but was '%s'.", expectedType.getSimpleName(), actualType.getSimpleName()));
        this.expectedType = expectedType;
        this.actualType = actualType;
    }

    public Class<?> getExpectedType() {
        return expectedType;
    }

    public Class<?> getActualType() {
        return actualType;
    }
}
