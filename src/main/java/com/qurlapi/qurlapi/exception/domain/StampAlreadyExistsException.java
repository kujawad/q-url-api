package com.qurlapi.qurlapi.exception.domain;

import com.qurlapi.qurlapi.exception.ApplicationException;
import com.qurlapi.qurlapi.exception.problem.ApplicationProblem;
import lombok.ToString;

@ToString
public class StampAlreadyExistsException extends ApplicationException {

    public StampAlreadyExistsException(final ApplicationProblem problem, final Object... messageParameters) {
        super(problem, messageParameters);
    }
}
