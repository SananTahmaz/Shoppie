package com.shoppie.payloads.user;

import com.shoppie.enums.UserGender;

import java.time.LocalDate;

public record UserUpdateRequest(
        String fullName,
        String bio,
        String phone,
        LocalDate birthDate,
        UserGender gender
) {
}
