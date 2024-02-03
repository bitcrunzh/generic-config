package com.bitcrunzh.generic.config.validation;

public class PropertyOptionalProblem extends PropertyProblem {
    private final Class<?> propertyType;

    public PropertyOptionalProblem(String propertyName, Class<?> propertyType) {
        super(ProblemSeverity.ERROR, propertyName, String.format("PropertyValue '%s:%s' was null, while its PropertyDescription describes it as not optional.", propertyName, propertyType.getSimpleName()));
        this.propertyType = propertyType;
    }

    public Class<?> getPropertyType() {
        return propertyType;
    }
}
