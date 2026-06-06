package com.shoppie.annotations;

import com.shoppie.validators.PermissibleAgeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PermissibleAgeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissibleAge {
    String message() default "Age must be at least 18";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
