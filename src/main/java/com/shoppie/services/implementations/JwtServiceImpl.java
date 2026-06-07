package com.shoppie.services.implementations;

import com.shoppie.jwt.UserPrincipal;
import com.shoppie.services.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    @Value("${spring.security.jwt.secret-key}")
    private String secretKey;

    @Value("${spring.security.jwt.access-token-expiration}")
    private Long accessTokenExpiration;

    @Value("${spring.security.jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    @Override
    public String generateAccessToken(UserPrincipal principal) {
        Instant now = Instant.now();
        Instant expiration = now.plusMillis(accessTokenExpiration);
        String jti = UUID.randomUUID().toString();

        return Jwts
                .builder()
                .id(jti)
                .subject(principal.getId().toString())
                .claim("email", principal.getEmail())
                .claim("role", principal.getRole().name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    @Override
    public String generateRefreshToken(UserPrincipal user) {
        Instant now = Instant.now();
        Instant expiration = now.plusMillis(refreshTokenExpiration);
        String jti = UUID.randomUUID().toString();

        return Jwts
                .builder()
                .id(jti)
                .id(UUID.randomUUID().toString())
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("role", user.getRole().name())
                .claim("type", "REFRESH")
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    @Override
    public Long extractUserId(String token) {
        String subject = Jwts
                .parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

        return Long.valueOf(subject);
    }

    @Override
    public String extractType(String token) {
        return Jwts
                .parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("type", String.class);
    }

    @Override
    public String extractJti(String token) {
        return Jwts
                .parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getId();
    }

    @Override
    public Boolean isValid(String token) {
        try {
            return !isExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean isExpired(String token) {
        Date expiration = Jwts
                .parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();

        return expiration.before(new Date());
    }

    @Override
    public Boolean isRefreshToken(String token) {
        return "REFRESH".equals(extractType(token));
    }

    @Override
    public Long remainingLifetime(String token) {
        Date expiration = Jwts
                .parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();

        return expiration.getTime() - System.currentTimeMillis();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
}
