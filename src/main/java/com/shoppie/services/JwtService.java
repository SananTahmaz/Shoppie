package com.shoppie.services;

import com.shoppie.jwt.UserPrincipal;

public interface JwtService {
    String generateAccessToken(UserPrincipal principal);

    String generateRefreshToken(UserPrincipal principal);

    Long extractUserId(String token);

    String extractType(String token);

    String extractJti(String token);

    Boolean isValid(String token);

    Boolean isExpired(String token);

    Boolean isRefreshToken(String token);

    Long remainingLifetime(String token);
}
