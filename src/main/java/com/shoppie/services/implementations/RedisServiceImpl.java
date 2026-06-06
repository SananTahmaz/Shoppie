package com.shoppie.services.implementations;

import com.shoppie.services.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {
    private final StringRedisTemplate template;

    @Override
    public void save(String key, String value, Long ttlSeconds) {
        template.opsForValue().set(key, value, Duration.ofSeconds(ttlSeconds));
    }

    @Override
    public String get(String key) {
        return template.opsForValue().get(key);
    }

    @Override
    public void delete(String key) {
        template.delete(key);
    }

    @Override
    public Boolean exists(String key) {
        return template.hasKey(key);
    }
}
