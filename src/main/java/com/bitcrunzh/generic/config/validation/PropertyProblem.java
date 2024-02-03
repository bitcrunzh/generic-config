package com.bitcrunzh.generic.config.validation;

public class PropertyProblem implements ValidationProblem {
    private final ProblemSeverity severity;
    private final String propertyName;
    private final String description;

    public PropertyProblem(ProblemSeverity severity, String propertyName, String description) {
        this.severity = severity;
        this.propertyName = propertyName;
        this.description = description;
    }

    @Override
    public ProblemSeverity getSeverity() {
        return severity;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
