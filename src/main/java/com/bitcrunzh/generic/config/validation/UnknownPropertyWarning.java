package com.bitcrunzh.generic.config.validation;

import java.util.Optional;

public class UnknownPropertyWarning extends PropertyProblem {
    public <V> UnknownPropertyWarning(String propertyName, Optional<V> value) {
        super(ProblemSeverity.WARNING, propertyName, String.format("Unknown property '%s%s' with value '%s' received. Ignored.", propertyName, getTypeDescription(value), getObjectValue(value)));
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static <V> String getObjectValue(Optional<V> value) {
        String objValue = "null";
        if (value.isPresent()) {
            objValue = value.get().toString();
        }
        return objValue;
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static <V> String getTypeDescription(Optional<V> value) {
        String typeDescription = "";
        if (value.isPresent()) {
            typeDescription = value.get().getClass().getSimpleName();
        }
        return typeDescription;
    }
}
