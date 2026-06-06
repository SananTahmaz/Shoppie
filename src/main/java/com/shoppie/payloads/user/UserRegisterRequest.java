package com.shoppie.payloads.user;

import com.shoppie.annotations.PermissibleAge;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UserRegisterRequest(
        @NotBlank(message = "Email is mandatory")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password is mandatory")
        String password,

        @NotBlank(message = "Confirm password is mandatory")
        String confirmPassword,

        @NotNull(message = "Birthdate is mandatory")
        @PermissibleAge
        LocalDate birthDate
) {
}
