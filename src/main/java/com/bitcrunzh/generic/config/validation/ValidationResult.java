package com.bitcrunzh.generic.config.validation;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ValidationResult<T> {
    private final T validatedObject;
    private final Map<ProblemSeverity, List<ValidationProblem>> severityProblemMap = new EnumMap<>(ProblemSeverity.class);
    private final ProblemSeverity highestSeverity;

    public ValidationResult(T validatedObject, List<ValidationProblem> problems) {
        this.validatedObject = validatedObject;
        ProblemSeverity highestSeverityFound = ProblemSeverity.NONE;
        for (ValidationProblem problem : problems) {
            severityProblemMap.put(problem.getSeverity(), problems);
            if (highestSeverityFound.getSeverityValue() < problem.getSeverity().getSeverityValue()) {
                highestSeverityFound = problem.getSeverity();
            }
        }
        this.highestSeverity = highestSeverityFound;
    }

    public boolean isValid() {
        return severityProblemMap.get(ProblemSeverity.ERROR).isEmpty();
    }

    public T getValidatedObject() {
        return validatedObject;
    }

    @Override
    public String toString() {
        return "ValidationResult{" +
                "errors=" + severityProblemMap.get(ProblemSeverity.ERROR) +
                ", warnings=" + severityProblemMap.get(ProblemSeverity.WARNING) +
                '}';
    }

    public ProblemSeverity getHighestSeverity() {
        return highestSeverity;
    }
}
