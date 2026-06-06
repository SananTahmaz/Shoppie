package com.shoppie.services.implementations;

import com.shoppie.exceptions.OtpRateLimitException;
import com.shoppie.services.RateLimiterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RateLimiterServiceImpl implements RateLimiterService {
    private final StringRedisTemplate redisTemplate;
    private static final String OTP_RESEND_KEY = "otp_cooldown:";
    private static final String OTP_ATTEMPTS_KEY = "otp_attempts:";
    private static final Integer MAX_ATTEMPTS = 5;
    private static final Duration ATTEMPT_WINDOW = Duration.ofMinutes(10);
    private static final Duration RESEND_COOLDOWN = Duration.ofSeconds(60);

    @Override
    public void checkOtpResendLimit(String email) {
        String key = OTP_RESEND_KEY + email;

        if (redisTemplate.hasKey(key)) {
            throw new OtpRateLimitException("Please wait 60 seconds before requesting new OTP");
        }

        redisTemplate.opsForValue().set(key, "BLOCKED", RESEND_COOLDOWN);
    }

    @Override
    public void checkOtpAttemptLimit(String email) {
        String key = OTP_ATTEMPTS_KEY + email;
        String value = redisTemplate.opsForValue().get(key);
        int attempts = value == null ? 0 : Integer.parseInt(value);

        if (attempts >= MAX_ATTEMPTS) {
            throw new OtpRateLimitException("Too many OTP attempts. Try again later");
        }
    }

    @Override
    public void recordFailedOtpAttempt(String email) {
        String key = OTP_ATTEMPTS_KEY + email;
        String value = redisTemplate.opsForValue().get(key);
        int attempts = value == null ? 0 : Integer.parseInt(value);
        attempts++;
        redisTemplate.opsForValue().set(key, String.valueOf(attempts), ATTEMPT_WINDOW);
    }

    @Override
    public void resetOtpAttempts(String email) {
        redisTemplate.delete(OTP_ATTEMPTS_KEY + email);
    }
}
