package com.shoppie.services.implementations;

import com.shoppie.services.RedisService;
import com.shoppie.entities.User;
import com.shoppie.enums.UserStatus;
import com.shoppie.exceptions.OtpCooldownException;
import com.shoppie.exceptions.ResourceNotFoundException;
import com.shoppie.payloads.otp.OtpResendRequest;
import com.shoppie.payloads.otp.OtpVerifyRequest;
import com.shoppie.repositories.UserRepository;
import com.shoppie.services.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {
    private final UserRepository userRepository;
    private final RedisService redisService;

    @Override
    public String generate() {
        return String.format("%06d", new Random().nextInt(1000000));
    }

    @Override
    public void verify(OtpVerifyRequest request) {
        String cachedOtp = redisService.get(String.format("otp:%s", request.email()));

        if (cachedOtp == null || !cachedOtp.equals(request.otp())) {
            throw new IllegalArgumentException("Invalid or expired OTP");
        }

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
        if (redisService.exists(String.format("otp_cooldown:%s", request.email()))) {
            throw new OtpCooldownException("Please wait 60 seconds before resending OTP");
        }

        String otp = generate();
        redisService.save(String.format("otp:%s", request.email()), otp, 300L);
        redisService.save(String.format("otp_cooldown:%s", request.email()), "true", 60L);
    }
}
