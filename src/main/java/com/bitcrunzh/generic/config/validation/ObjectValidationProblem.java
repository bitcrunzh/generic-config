package com.bitcrunzh.generic.config.validation;

public class ObjectValidationProblem implements ValidationProblem {
    private final ProblemSeverity severity;
    private final String description;

    public ObjectValidationProblem(ProblemSeverity severity, String description) {
        this.severity = severity;
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
}
