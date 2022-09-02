package com.qurlapi.qurlapi.exception.validation;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.metadata.ConstraintDescriptor;
import java.util.List;
import java.util.Map;

public final class ValidationErrorFactory {

    public static ValidationError create(final FieldError fieldError) {
        return new ValidationError.Builder().withMessageKey(MessageKeyMapper.map(fieldError.getCode()))
                                            .withContextKey(fieldError.getField())
                                            .withMessage(fieldError.getDefaultMessage())
                                            .build();
    }

    public static ValidationError create(final ConstraintViolation<?> violation) {
        return new ValidationError.Builder().withMessageKey(MessageKeyMapper.map(violation.getConstraintDescriptor()
                                                                                       .getAnnotation()
                                                                                       .annotationType()
                                                                                       .getSimpleName()))
                                            .withContextKey(getPropertyName(violation))
                                            .withMessage(violation.getMessage())
                                            .build();
    }

    public static List<ValidationError> create(final List<ObjectError> errors) {
        return errors.stream()
                     .filter(violation -> violation instanceof FieldError)
                     .map(violation -> create((FieldError) violation))
                     .toList();
    }

    public static List<ValidationError> create(final ConstraintViolationException exception) {
        return exception.getConstraintViolations()
                        .stream()
                        .map(ValidationErrorFactory::create)
                        .toList();
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
