package com.qurlapi.qurlapi.exception.handler;

import com.qurlapi.qurlapi.dto.response.ProblemResponse;
import com.qurlapi.qurlapi.exception.domain.StampNotFoundException;
import com.qurlapi.qurlapi.exception.handler.message.ValidationLogMessageFactory;
import com.qurlapi.qurlapi.exception.problem.Problem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler implements BaseExceptionHandler {

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<Problem> handleException(final BindException exception) {
        return createResponseEntity(HttpStatus.BAD_REQUEST, ValidationLogMessageFactory.create(exception), List.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Problem> handleException(final MethodArgumentNotValidException exception) {
        return createResponseEntity(HttpStatus.BAD_REQUEST, ValidationLogMessageFactory.create(exception), List.of());
    }

    @ExceptionHandler(StampNotFoundException.class)
    protected ResponseEntity<Problem> handleException(final StampNotFoundException exception) {
        final Problem problem = exception.getProblem();
        final ProblemResponse response = new ProblemResponse.Builder().withTitle(problem.getTitle())
                                                                      .withDetail(exception.getMessage())
                                                                      .withStatus(problem.getStatus())
                                                                      .withType(problem.getType()
                                                                                       .toString())
                                                                      .build();
        return createResponseEntity(response);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemResponse handleException(final HttpServletRequest request, final ConstraintViolationException ex) {
        return null;

        /*final ProblemResponse.ProblemResponseBuilder responseBuilder = ProblemResponse.builder();

        ex.getConstraintViolations()
          .stream()
          .findFirst()
          .ifPresent(constraintViolation -> responseBuilder.status(HttpStatus.NOT_FOUND.value())
                                                           .path(request.getServletPath())
                                                           .message(constraintViolation.getMessage())
                                                           .timestamp(new Date().getTime())
                                                           .build());
        return responseBuilder.build();*/
    }


}
