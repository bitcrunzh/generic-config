package com.bitcrunzh.generic.config.validation;

import java.util.*;

public class ValidationResult<T> {
    public static final ValidationResult<?> EMPTY = new ValidationResult<>();
    private final T validatedObject;
    private final Map<ProblemSeverity, List<ValidationProblem>> severityProblemMap = new EnumMap<>(ProblemSeverity.class);
    private ProblemSeverity highestSeverity;

    public ValidationResult() {
        this(null, Collections.emptyList());
    }

    public ValidationResult(T validatedObject) {
        this(validatedObject, Collections.emptyList());
    }

    public ValidationResult(ValidationProblem problems) {
        this(null, Collections.singletonList(problems));
    }

    public ValidationResult(T validatedObject, ValidationProblem problem) {
        this(validatedObject, Collections.singletonList(problem));
    }

    public ValidationResult(T validatedObject, List<ValidationProblem> problems) {
        this.validatedObject = validatedObject;
        for (ValidationProblem problem : problems) {
            addValidationProblem(problem);
        }
    }

    public ValidationResult(T validatedObject, ValidationResult<?> validationResult) {
        this.validatedObject = validatedObject;
        addValidationResult(validationResult);
    }

    public static <T> ValidationResult<T> empty() {
        return new ValidationResult<>();
    }

    public static <T> ValidationResult<T> of(ValidationProblem problem) {
        return new ValidationResult<>(problem);
    }

    public boolean isValid() {
        return severityProblemMap.get(ProblemSeverity.ERROR).isEmpty();
    }

    public boolean hasErrors() {
        return !isValid();
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

    public void addValidationResult(ValidationResult<?> validationResult) {
        if(validationResult.highestSeverity.getSeverityValue() > highestSeverity.getSeverityValue()) {
            highestSeverity = validationResult.highestSeverity;
        }
        for(Map.Entry<ProblemSeverity, List<ValidationProblem>> severityValidation : validationResult.severityProblemMap.entrySet()) {
            List<ValidationProblem> validationProblems = severityProblemMap.computeIfAbsent(severityValidation.getKey(), key -> new ArrayList<>());
            validationProblems.addAll(severityValidation.getValue());
        }
    }

    public void addValidationProblem(ValidationProblem validationProblem) {
        if(validationProblem.getSeverity().getSeverityValue() > highestSeverity.getSeverityValue()) {
            highestSeverity = validationProblem.getSeverity();
        }
        severityProblemMap.computeIfAbsent(validationProblem.getSeverity(), key -> new ArrayList<>()).add(validationProblem);
    }
}
