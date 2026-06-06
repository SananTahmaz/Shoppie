package com.shoppie.services;

public interface RateLimiterService {
    void checkOtpResendLimit(String email);

    void checkOtpAttemptLimit(String email);

    void recordFailedOtpAttempt(String email);

    void resetOtpAttempts(String email);
}
