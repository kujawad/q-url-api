package com.qurlapi.qurlapi.exception.service;

import com.qurlapi.qurlapi.exception.ApplicationException;
import com.qurlapi.qurlapi.exception.problem.ApplicationProblem;
import lombok.ToString;

@ToString
public class ServiceException extends ApplicationException {

    public ServiceException(final ApplicationProblem problem, final Object... messageParameters) {
        super(problem, messageParameters);
    }
}
