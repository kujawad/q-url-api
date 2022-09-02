package com.qurlapi.qurlapi.exception;

import com.qurlapi.qurlapi.exception.problem.ApplicationProblem;
import lombok.Getter;

import java.text.MessageFormat;

@Getter
public abstract class ApplicationException extends RuntimeException {

    private final ApplicationProblem problem;
    private final Object[] messageParameters;

    public ApplicationException(final ApplicationProblem problem, final Object... messageParameters) {
        super(MessageFormat.format(problem.getMessage(), messageParameters));
        this.problem = problem;
        this.messageParameters = messageParameters;
    }
}
