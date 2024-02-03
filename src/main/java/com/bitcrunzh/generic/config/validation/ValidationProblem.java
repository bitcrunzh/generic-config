package com.bitcrunzh.generic.config.validation;

public interface ValidationProblem {
    ProblemSeverity getSeverity();
    String getDescription();
}
