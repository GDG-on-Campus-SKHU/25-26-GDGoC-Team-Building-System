package com.skhu.gdgocteambuildingproject.global.email.service;

import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    private final StringRedisTemplate redisTemplate;
    private static final String PREFIX = "emailCode:";
    private static final long EXPIRE_MINUTES = 5;

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    private String key(String email) {
        return PREFIX + normalizeEmail(email);
    }

    public void saveCode(String email, String code) {
        redisTemplate.opsForValue()
                .set(key(email), code, EXPIRE_MINUTES, TimeUnit.MINUTES);
    }

    public void validateCode(String email, String inputCode) {
        if (email == null || email.isBlank()
                || inputCode == null || inputCode.isBlank()) {
            throw new IllegalArgumentException(
                    ExceptionMessage.EMAIL_INVALID_FORMAT.getMessage()
            );
        }

        String storedCode = redisTemplate.opsForValue().get(key(email));

        if (storedCode == null) {
            throw new IllegalArgumentException(
                    ExceptionMessage.VERIFICATION_CODE_EXPIRED.getMessage()
            );
        }

        if (!storedCode.equals(inputCode)) {
            throw new IllegalArgumentException(
                    ExceptionMessage.VERIFICATION_CODE_INVALID.getMessage()
            );
        }
    }

    public void consumeCode(String email) {
        redisTemplate.delete(key(email));
    }
}
