package com.shoppie.services;

import com.shoppie.payloads.authentication.AuthResponse;
import com.shoppie.payloads.authentication.RefreshTokenRequest;
import com.shoppie.payloads.authentication.UserLoginRequest;
import com.shoppie.payloads.authentication.UserLogoutRequest;

public interface AuthService {
    AuthResponse login(UserLoginRequest request);
    AuthResponse refreshToken(RefreshTokenRequest request);
    void logout(UserLogoutRequest request);
}
