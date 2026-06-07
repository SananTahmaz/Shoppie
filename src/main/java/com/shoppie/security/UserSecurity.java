package com.shoppie.security;

import com.shoppie.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("userSecurity")
@RequiredArgsConstructor
public class UserSecurity {
    private final SecurityUtils securityUtils;

    public boolean isOwner(Long userId) {
        return securityUtils.currentUserId().equals(userId);
    }
}
