package com.qurlapi.qurlapi.exception.handler;

import com.qurlapi.qurlapi.dto.response.ProblemResponse;
import com.qurlapi.qurlapi.exception.problem.Problem;
import com.qurlapi.qurlapi.exception.validation.ValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

interface BaseExceptionHandler {
    default ResponseEntity<Problem> createResponseEntity(final HttpStatus httpStatus) {
        return createResponseEntity(new ProblemResponse(httpStatus));
    }

    default ResponseEntity<Problem> createResponseEntity(final ProblemResponse problemResponse) {
        return createResponseEntity(problemResponse, problemResponse.getStatus());

    }

    default ResponseEntity<Problem> createResponseEntity(final ProblemResponse problem, final Integer httpStatus) {
        return ResponseEntity.status(httpStatus)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(problem);
    }

    default ResponseEntity<Problem> createResponseEntity(final HttpStatus httpStatus, final String message,
                                                         final ValidationError validationError) {
        return createResponseEntity(httpStatus, message, Collections.singletonList(validationError));
    }

    default ResponseEntity<Problem> createResponseEntity(final HttpStatus httpStatus, final String message,
                                                         final List<ValidationError> validationErrors) {
        return createResponseEntity(new ProblemResponse.Builder().withType("common:validation-error")
                                                                 .withTitle("Request parameters are not valid")
                                                                 .withDetail(message)
                                                                 .withStatus(httpStatus.value())
                                                                 .withValidationErrors(validationErrors)
                                                                 .build());
    }


}
