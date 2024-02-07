package com.bitcrunzh.generic.config.validation;

public class UnknownClassDescriptionProblem extends PropertyProblem{

    public UnknownClassDescriptionProblem(Class<?> parentType, Class<?> type, String propertyName) {
        super(ProblemSeverity.ERROR, propertyName, String.format("ClassDescription could not be found for property '%s.%s' of type '%s'", parentType.getSimpleName(), propertyName, type.getSimpleName()));
    }
}
