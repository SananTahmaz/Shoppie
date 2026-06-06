package com.shoppie.services;

public interface RedisService {
    void save(String key, String value, Long ttlSeconds);

    String get(String key);

    void delete(String key);
}
