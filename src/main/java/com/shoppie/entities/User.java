package com.shoppie.entities;

import com.shoppie.enums.UserGender;
import com.shoppie.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
    private UserGender gender;

    @Column(nullable = false)
    private UserRole role;
}
