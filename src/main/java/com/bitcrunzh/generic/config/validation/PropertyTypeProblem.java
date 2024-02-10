package com.bitcrunzh.generic.config.validation;

import com.bitcrunzh.generic.config.util.PrintUtil;

import java.util.Collection;
import java.util.Collections;

public class PropertyTypeProblem extends PropertyProblem {
    private final Collection<Class<?>> expectedTypes;
    private final Class<?> actualType;

    public PropertyTypeProblem(String propertyName, Class<?> expectedType, Class<?> actualType) {
        this(propertyName, Collections.singleton(expectedType), actualType);
    }

    public PropertyTypeProblem(String propertyName, Collection<Class<?>> expectedTypes, Class<?> actualType) {
        super(ProblemSeverity.ERROR, propertyName, String.format("PropertyValue of type '%s', cannot be assigned to property '%s:%s' as the type is not the same or a subtype.", actualType.getSimpleName(),propertyName, PrintUtil.printSimpleClassNames(expectedTypes)));
        this.expectedTypes = expectedTypes;
        this.actualType = actualType;
    }

    public Collection<Class<?>> getExpectedType() {
        return expectedTypes;
    }

    public Class<?> getActualType() {
        return actualType;
    }
}
