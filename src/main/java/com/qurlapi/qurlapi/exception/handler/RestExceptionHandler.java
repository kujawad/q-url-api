package com.qurlapi.qurlapi.exception.handler;

import com.qurlapi.qurlapi.dto.response.ProblemResponse;
import com.qurlapi.qurlapi.exception.ApplicationException;
import com.qurlapi.qurlapi.exception.domain.StampAlreadyExistsException;
import com.qurlapi.qurlapi.exception.domain.StampNotFoundException;
import com.qurlapi.qurlapi.exception.handler.message.ConstraintViolationLogMessageFactory;
import com.qurlapi.qurlapi.exception.handler.message.HttpMessageNotReadableLogMessageFactory;
import com.qurlapi.qurlapi.exception.handler.message.ValidationLogMessageFactory;
import com.qurlapi.qurlapi.exception.problem.Problem;
import com.qurlapi.qurlapi.exception.validation.ValidationErrorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Collections;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler implements BaseExceptionHandler {

    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    protected ResponseEntity<Problem> handleException(final BindException exception) {
        log.warn("{}{}", exception.getMessage(), exception);
        return createResponseEntity(HttpStatus.BAD_REQUEST, ValidationLogMessageFactory.create(exception),
                                    ValidationErrorFactory.create(exception.getAllErrors()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Problem> handleException(final ConstraintViolationException exception) {
        log.warn("{}{}", exception.getMessage(), exception);
        return createResponseEntity(HttpStatus.BAD_REQUEST, ConstraintViolationLogMessageFactory.create(exception),
                                    ValidationErrorFactory.create(exception));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Problem> handleException(final HttpMessageNotReadableException exception) {
        log.warn("{}{}", exception.getMessage(), exception);
        return createResponseEntity(HttpStatus.BAD_REQUEST, HttpMessageNotReadableLogMessageFactory.create(exception),
                                    Collections.emptyList());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Problem> handleException(final HttpRequestMethodNotSupportedException exception) {
        log.warn("{}{}", exception.getMessage(), exception);
        return createResponseEntity(HttpStatus.METHOD_NOT_ALLOWED, exception.getMessage(), Collections.emptyList());
    }

    @ExceptionHandler({StampNotFoundException.class, StampAlreadyExistsException.class})
    protected ResponseEntity<Problem> handleException(final ApplicationException exception) {
        final Problem problem = exception.getProblem();
        final ProblemResponse response = new ProblemResponse.Builder().withTitle(problem.getTitle())
                                                                      .withDetail(exception.getMessage())
                                                                      .withStatus(problem.getStatus())
                                                                      .withType(problem.getType()
                                                                                       .toString())
                                                                      .build();
        return createResponseEntity(response);
    }
}
