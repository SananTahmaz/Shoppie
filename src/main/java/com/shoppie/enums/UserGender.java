package com.shoppie.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum UserGender {
    MALE,
    FEMALE;

    @JsonCreator
    public static UserGender from(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Gender cannot be null");
        }

        try {
            return UserGender.valueOf(value.trim().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    String.format("Invalid gender: %s. Allowed values: MALE, FEMALE", value)
            );
        }
    }
}
