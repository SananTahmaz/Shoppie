package com.shoppie.payloads.authentication;

import jakarta.validation.constraints.NotBlank;

public record UserLogoutRequest(
        @NotBlank(message = "Access token is mandatory")
        String accessToken,

        @NotBlank(message = "Refresh token is mandatory")
        String refreshToken
) {
}
