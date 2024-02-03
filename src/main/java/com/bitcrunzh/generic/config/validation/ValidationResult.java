package com.bitcrunzh.generic.config.validation;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ValidationResult<T> {
    private final T validatedObject;
    private final Map<ProblemSeverity, List<ValidationProblem>> severityProblemMap = new EnumMap<>(ProblemSeverity.class);

    public ValidationResult(T validatedObject, List<ValidationProblem> problems) {
        this.validatedObject = validatedObject;
        for (ValidationProblem problem : problems) {
            severityProblemMap.put(problem.getSeverity(), problems);
        }
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
}
