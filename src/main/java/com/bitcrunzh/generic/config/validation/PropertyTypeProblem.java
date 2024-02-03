package com.bitcrunzh.generic.config.validation;

public class PropertyTypeProblem extends PropertyProblem {
    private final Class<?> expectedType;
    private final Class<?> actualType;

    public PropertyTypeProblem(String propertyName, Class<?> expectedType, Class<?> actualType) {
        super(ProblemSeverity.ERROR, propertyName, String.format("PropertyValue of type '%s', cannot be assigned to property '%s:%s' as the type is not the same or a subtype.", actualType.getSimpleName(),propertyName, expectedType.getSimpleName()));
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
