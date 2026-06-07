package com.shoppie.services;

public interface TokenBlacklistService {
    void blacklist(String jti, Long ttl);
    Boolean isBlacklisted(String jti);
}
