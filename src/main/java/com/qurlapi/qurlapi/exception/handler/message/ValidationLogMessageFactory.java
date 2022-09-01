package com.qurlapi.qurlapi.exception.handler.message;

import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;

public final class ValidationLogMessageFactory {

    public static String create(final BindException exception) {
        return BindExceptionLogMessageFactory.create(exception);
    }

    public static String create(final MethodArgumentNotValidException exception) {
        return BindExceptionLogMessageFactory.create(exception.getBindingResult());
    }
}
