package com.skhu.gdgocteambuildingproject.global.email.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final StringRedisTemplate redisTemplate;

    public void saveCode(String email, String code) {
        redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);
    }

    public boolean verifyCode(String email, String inputCode) {
        String storedCode = redisTemplate.opsForValue().get(email);
        boolean isValid = storedCode != null && storedCode.equals(inputCode);

        if (isValid) {
            markVerified(email);
            redisTemplate.delete(email);
        }

        return isValid;
    }

    public void markVerified(String email) {
        redisTemplate.opsForValue().set("verified:" + email, "true", 10, TimeUnit.MINUTES);
    }

    public boolean isVerified(String email) {
        String verified = redisTemplate.opsForValue().get("verified:" + email);
        return "true".equals(verified);
    }

    public void deleteVerifiedStatus(String email) {
        redisTemplate.delete("verified:" + email);
    }
}
