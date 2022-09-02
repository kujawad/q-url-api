package com.qurlapi.qurlapi.exception.handler;

import com.qurlapi.qurlapi.exception.problem.Problem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ApplicationExceptionHandler implements BaseExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Problem> handleException(final Throwable throwable) {
        log.warn("Unexpected server exception: {}", throwable.getMessage(), throwable);
        return createResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
