package com.shoppie.payloads.user;

import com.shoppie.enums.UserGender;
import com.shoppie.enums.UserRole;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String fullName,
        String bio,
        String phone,
        String email,
        LocalDate birthDate,
        UserGender gender,
        UserRole role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
