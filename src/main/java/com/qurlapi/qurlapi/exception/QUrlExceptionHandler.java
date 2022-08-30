package com.qurlapi.qurlapi.exception;

import com.qurlapi.qurlapi.dto.response.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.Objects;

@RestControllerAdvice
public class QUrlExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(QUrlExceptionHandler.class);

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                  @NonNull final HttpHeaders headers,
                                                                  final HttpStatus status,
                                                                  @NonNull final WebRequest request) {
        final HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();

        final ExceptionResponse response = ExceptionResponse.builder()
                                                            .status(status.value())
                                                            .path(servletRequest.getServletPath())
                                                            .message(Objects.requireNonNull(ex.getBindingResult()
                                                                                              .getFieldError())
                                                                            .getDefaultMessage())
                                                            .timestamp(new Date().getTime())
                                                            .build();

        LOGGER.error(response.toString());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleStampNotFound(final HttpServletRequest request,
                                                 final ConstraintViolationException ex) {
        final ExceptionResponse.ExceptionResponseBuilder responseBuilder =
                ExceptionResponse.builder();

        ex.getConstraintViolations()
          .stream()
          .findFirst()
          .ifPresent(constraintViolation ->
                             responseBuilder.status(HttpStatus.NOT_FOUND.value())
                                            .path(request.getServletPath())
                                            .message(constraintViolation.getMessage())
                                            .timestamp(new Date().getTime())
                                            .build()
                    );
        final ExceptionResponse response = responseBuilder.build();

        LOGGER.error(response.toString());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
