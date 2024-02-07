package com.bitcrunzh.generic.config.validation;

public enum ProblemSeverity {
    ERROR(3),
    WARNING(2),
    NONE(1);

    private final int severityValue;

    ProblemSeverity(int severityValue) {
        this.severityValue = severityValue;
    }

    public int getSeverityValue() {
        return severityValue;
    }
}
