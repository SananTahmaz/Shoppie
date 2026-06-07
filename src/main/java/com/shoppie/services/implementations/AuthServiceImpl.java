package com.shoppie.services.implementations;

import com.shoppie.entities.User;
import com.shoppie.exceptions.InvalidRefreshTokenException;
import com.shoppie.exceptions.InvalidTokenException;
import com.shoppie.exceptions.TokenRevokedException;
import com.shoppie.jwt.UserPrincipal;
import com.shoppie.payloads.authentication.AuthResponse;
import com.shoppie.payloads.authentication.RefreshTokenRequest;
import com.shoppie.payloads.authentication.UserLoginRequest;
import com.shoppie.payloads.authentication.UserLogoutRequest;
import com.shoppie.repositories.UserRepository;
import com.shoppie.services.AuthService;
import com.shoppie.services.JwtService;
import com.shoppie.services.RedisService;
import com.shoppie.services.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RedisService redisService;
    private final UserRepository userRepository;
    private final TokenBlacklistService tokenBlacklistService;

    @Override
    public AuthResponse login(UserLoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(principal);
        String refreshToken = jwtService.generateRefreshToken(principal);
        redisService.save(
                String.format("refresh:%s", principal.getId()),
                refreshToken,
                604800L
        );
        return new AuthResponse(accessToken, refreshToken);
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.refreshToken();

        if (!jwtService.isValid(refreshToken)) {
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        if (!jwtService.isRefreshToken(refreshToken)) {
            throw new InvalidTokenException("Invalid token type");
        }

        Long userId = jwtService.extractUserId(refreshToken);
        String storedToken = redisService.get(String.format("refresh:%s", userId));

        if (storedToken == null) {
            throw new InvalidRefreshTokenException("Refresh token not found");
        }

        if (!storedToken.equals(refreshToken)) {
            throw new TokenRevokedException("Refresh token revoked");
        }

        User user = userRepository.findById(userId).orElseThrow();

        UserPrincipal principal = new UserPrincipal(
                user.getId(), user.getEmail(), user.getEncodedPassword(), user.getRole()
        );

        String newAccessToken = jwtService.generateAccessToken(principal);
        String newRefreshToken = jwtService.generateRefreshToken(principal);

        redisService.save(
                String.format("refresh:%s", userId),
                newRefreshToken,
                604800L
        );

        return new AuthResponse(newAccessToken, newRefreshToken);
    }

    @Override
    public void logout(UserLogoutRequest request) {
        String accessJti = jwtService.extractJti(request.accessToken());
        tokenBlacklistService.blacklist(accessJti, jwtService.remainingLifetime(request.accessToken()));
        String refreshJti = jwtService.extractJti(request.accessToken());
        redisService.delete(String.format("refresh:%s", refreshJti));
    }
}
