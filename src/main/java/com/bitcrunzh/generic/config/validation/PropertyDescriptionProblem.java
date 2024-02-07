package com.bitcrunzh.generic.config.validation;

public class PropertyDescriptionProblem extends PropertyProblem {
    public <C, T> PropertyDescriptionProblem(Class<C> parentType, Class<T> type, String propertyName, Class<?> expectedValueType, Class<?> actualValueType) {
        super(ProblemSeverity.ERROR, propertyName,createDescription(parentType, type, propertyName, expectedValueType, actualValueType));

    }

    public static String createDescription(Class<?> parentType, Class<?> type, String propertyName, Class<?> expectedValueType, Class<?> actualValueType) {
        return String.format("Property '%s.%s:%s was not wrapped in correct Value type. Expected '%s' but was '%s'.",
                parentType.getSimpleName(),
                type.getSimpleName(),
                propertyName,
                expectedValueType.getSimpleName(),
                actualValueType.getSimpleName()
        );
    }
}
