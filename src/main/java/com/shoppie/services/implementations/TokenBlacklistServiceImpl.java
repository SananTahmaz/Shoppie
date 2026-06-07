package com.shoppie.services.implementations;

import com.shoppie.services.RedisService;
import com.shoppie.services.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenBlacklistServiceImpl implements TokenBlacklistService {
    private final RedisService redisService;

    @Override
    public void blacklist(String jti, Long ttl) {
        redisService.save(String.format("blacklist:%s", jti), String.valueOf(true), ttl);
    }

    @Override
    public Boolean isBlacklisted(String jti) {
        return redisService.exists(String.format("blacklist:%s", jti));
    }
}
