package com.qurlapi.qurlapi.exception.problem;

import org.springframework.http.HttpStatus;

import java.net.URI;
import java.util.Map;

public interface ApplicationProblem extends Problem {

    @Override
    default URI getType() {
        return null;
    }

    @Override
    default String getTitle() {
        return getHttpStatus().getReasonPhrase();
    }

    @Override
    default Integer getStatus() {
        return getHttpStatus().value();
    }

    @Override
    default Map<String, Object> getExtensions() {
        return null;
    }

    HttpStatus getHttpStatus();
}
