package com.shoppie.validators;

import com.shoppie.annotations.PermissibleAge;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class PermissibleAgeValidator implements ConstraintValidator<PermissibleAge, LocalDate> {
    private final static Integer PERMISSIBLE_AGE = 18;

    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
        if (birthDate == null) {
            return false;
        }

        return Period.between(birthDate, LocalDate.now()).getYears() >= PERMISSIBLE_AGE;
    }
}
