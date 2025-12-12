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

    public void saveCode(String email, String code) {
        redisTemplate.opsForValue()
                .set(PREFIX + email, code, 5, TimeUnit.MINUTES);
    }

    public void verifyCode(String email, String inputCode) {
        validateInput(email, inputCode);

        String key = PREFIX + email;
        String storedCode = redisTemplate.opsForValue().get(key);

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

        redisTemplate.delete(key);
    }

    private void validateInput(String email, String code) {
        if (email == null || email.isBlank() ||
                code == null || code.isBlank()) {
            throw new IllegalArgumentException(
                    ExceptionMessage.EMAIL_INVALID_FORMAT.getMessage()
            );
        }
    }
}
