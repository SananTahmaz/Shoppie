package com.shoppie.services.implementations;

import com.shoppie.services.EmailService;
import com.shoppie.services.RateLimiterService;
import com.shoppie.services.RedisService;
import com.shoppie.entities.User;
import com.shoppie.enums.UserStatus;
import com.shoppie.exceptions.commons.ResourceNotFoundException;
import com.shoppie.payloads.otp.OtpResendRequest;
import com.shoppie.payloads.otp.OtpVerifyRequest;
import com.shoppie.repositories.UserRepository;
import com.shoppie.services.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {
    private final UserRepository userRepository;
    private final RateLimiterService limiterService;
    private final RedisService redisService;
    private final EmailService emailService;

    @Override
    public String generate() {
        return String.format("%06d", new Random().nextInt(1000000));
    }

    @Override
    public void verify(OtpVerifyRequest request) {
        limiterService.checkOtpAttemptLimit(request.email());
        String cachedOtp = redisService.get(String.format("otp:%s", request.email()));

        if (cachedOtp == null || !cachedOtp.equals(request.otp())) {
            limiterService.recordFailedOtpAttempt(request.email());
            throw new IllegalArgumentException("Invalid or expired OTP");
        }

        limiterService.resetOtpAttempts(request.email());

        User user = userRepository
                .findByEmailIgnoreCase(request.email())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                String.format("User not found with email: %s", request.email())
                        )
                );

        user.setStatus(UserStatus.ACTIVE);
        user.setActivatedAt(LocalDateTime.now());
        userRepository.save(user);
        redisService.delete(String.format("otp:%s", request.email()));
        redisService.delete(String.format("otp_cooldown:%s", request.email()));
    }

    @Override
    public void resend(OtpResendRequest request) {
        limiterService.checkOtpResendLimit(request.email());

        User user = userRepository
                .findByEmailIgnoreCase(request.email())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                String.format("User not found with email: %s", request.email())
                        )
                );

        String otp = generate();
        redisService.save(String.format("otp:%s", request.email()), otp, 300L);
        redisService.save(String.format("otp_cooldown:%s", request.email()), "true", 60L);
        emailService.sendOtp(user.getEmail(), otp);
    }
}
