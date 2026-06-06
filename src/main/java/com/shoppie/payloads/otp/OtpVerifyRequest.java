package com.shoppie.payloads.otp;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record OtpVerifyRequest(
        @NotBlank(message = "Email is mandatory")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "OTP is mandatory")
        String otp
) {
}
