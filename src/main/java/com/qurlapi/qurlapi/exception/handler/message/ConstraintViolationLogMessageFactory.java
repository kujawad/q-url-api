package com.qurlapi.qurlapi.exception.handler.message;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.metadata.ConstraintDescriptor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class ConstraintViolationLogMessageFactory {
    public static String create(final ConstraintViolationException exception) {
        final Set<? extends ConstraintViolation<?>> violations = exception.getConstraintViolations();
        final StringBuilder builder = new StringBuilder("Validation failed: ");
        builder.append("parameter errors (")
               .append(violations.size())
               .append(") on [");

        final Map<String, Integer> parameterErrors = getParameterErrors(violations);
        parameterErrors.forEach((key, value) -> builder.append("'")
                                                       .append(key)
                                                       .append("'(")
                                                       .append(value)
                                                       .append("), "));
        builder.delete(builder.length() - 2, builder.length());
        builder.append("]");
        return builder.toString();
    }

    private static Map<String, Integer> getParameterErrors(
            final Set<? extends ConstraintViolation<?>> constraintViolations) {
        final Map<String, Integer> parameterErrors = new HashMap<>();
        constraintViolations.stream()
                            .map(ConstraintViolationLogMessageFactory::getPropertyName)
                            .forEach(propertyName -> {
                                final Integer errorSize = parameterErrors.getOrDefault(propertyName, 0);
                                parameterErrors.put(propertyName, errorSize + 1);
                            });
        return parameterErrors;
    }

    private static String getPropertyName(final ConstraintViolation<?> constraintViolation) {
        final ConstraintDescriptor<?> constraintDescriptor = constraintViolation.getConstraintDescriptor();
        final Map<String, Object> attributes = constraintDescriptor.getAttributes();
        final String name = (String) attributes.get("name");

        return StringUtils.hasText(name) ? name : resolvePathPropertyName(constraintViolation);
    }

    private static String resolvePathPropertyName(final ConstraintViolation<?> constraintViolation) {
        final PathImpl propertyPath = (PathImpl) constraintViolation.getPropertyPath();
        return propertyPath.getLeafNode()
                           .getName();
    }
}