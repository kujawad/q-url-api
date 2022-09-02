package com.qurlapi.qurlapi.exception.handler.message;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;

import java.util.List;

public final class HttpMessageNotReadableLogMessageFactory {

    public static String create(final HttpMessageNotReadableException exception) {
        final Throwable cause = exception.getCause();
        final StringBuilder builder = new StringBuilder("Validation failed for key '");

        if (cause instanceof final MismatchedInputException mismatchedInputException) {
            builder.append(getFieldName(mismatchedInputException));
        }
        builder.append("'");

        return builder.toString();
    }

    private static String getFieldName(final MismatchedInputException cause) {
        final List<JsonMappingException.Reference> referenceList = cause.getPath();
        final String unknownParameter = "'unknown parameter'";
        if (!referenceList.isEmpty()) {
            final JsonMappingException.Reference reference = referenceList.get(0);
            final String name = reference.getFieldName();
            return StringUtils.hasText(name) ? name : unknownParameter;
        }
        return unknownParameter;
    }
}
