package com.shoppie.controllers;

import com.shoppie.globals.ApiResponse;
import com.shoppie.payloads.authentication.AuthResponse;
import com.shoppie.payloads.authentication.RefreshTokenRequest;
import com.shoppie.payloads.authentication.UserLoginRequest;
import com.shoppie.payloads.authentication.UserLogoutRequest;
import com.shoppie.services.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "AuthController", description = "Authentication features for Shoppie")
public class AuthController {
    private final AuthService service;

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody UserLoginRequest request) {
        AuthResponse response = service.login(request);
        return ApiResponse.success("User logged in successfully", response);
    }

    @PostMapping("/refresh-token")
    public ApiResponse<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        AuthResponse response = service.refreshToken(request);
        return ApiResponse.success("Token refreshed successfully", response);
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@Valid @RequestBody UserLogoutRequest request) {
        service.logout(request);
        return ApiResponse.success("User logged out successfully", null);
    }
}
