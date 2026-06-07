package com.shoppie.payloads.authentication;

public record AuthResponse(String accessToken, String refreshToken) {
}
