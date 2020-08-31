package com.qurlapi.qurlapi.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = StampExistingValidator.class)
public @interface StampExists {
    String message() default "Stamp does not exists!";

    boolean isPathVariable() default false;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
