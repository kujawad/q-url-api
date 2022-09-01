package com.qurlapi.qurlapi.exception.handler.message;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class BindExceptionLogMessageFactory {
    public static String create(final BindingResult bindingResult) {
        final StringBuilder builder = new StringBuilder("Validation failed for object '");
        builder.append(bindingResult.getObjectName())
               .append("'");

        if (bindingResult.getErrorCount() == 0) {
            return builder.toString();
        }

        builder.append(":");

        if (bindingResult.hasGlobalErrors()) {
            builder.append(" global errors (")
                   .append(bindingResult.getGlobalErrorCount())
                   .append("),");
        }

        if (bindingResult.hasFieldErrors()) {
            builder.append(createFieldErrors(bindingResult));
        } else {
            builder.delete(builder.length() - 1, builder.length());
        }

        return builder.toString();
    }

    private static String createFieldErrors(final BindingResult bindingResult) {
        final StringBuilder builder = new StringBuilder(" field errors (").append(bindingResult.getFieldErrorCount())
                                                                          .append(") on [");
        final Set<String> errorFields = getErrorFields(bindingResult.getFieldErrors());
        for (final String field : errorFields) {
            builder.append("'")
                   .append(field)
                   .append("'(")
                   .append(bindingResult.getFieldErrorCount(field))
                   .append("), ");
        }

        builder.delete(builder.length() - 2, builder.length());
        builder.append("]");
        return builder.toString();
    }

    private static Set<String> getErrorFields(final List<FieldError> fieldErrors) {
        return fieldErrors.stream()
                          .map(FieldError::getField)
                          .collect(Collectors.toSet());
    }
}