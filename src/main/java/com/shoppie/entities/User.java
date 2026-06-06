package com.shoppie.entities;

import com.shoppie.enums.UserGender;
import com.shoppie.enums.UserRole;
import com.shoppie.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Column(length = 100)
    private String fullName;

    @Column(length = 1000)
    private String bio;
    private String phone;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String encodedPassword;
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private UserGender gender;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;
}
