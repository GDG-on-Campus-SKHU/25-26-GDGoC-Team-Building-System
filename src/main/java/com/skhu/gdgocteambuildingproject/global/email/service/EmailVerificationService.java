package com.skhu.gdgocteambuildingproject.global.email.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final StringRedisTemplate redisTemplate;

    // 인증번호 저장 (5분 유효)
    public void saveCode(String email, String code) {
        redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);
    }

    // 인증번호 검증
    public boolean verifyCode(String email, String inputCode) {
        String storedCode = redisTemplate.opsForValue().get(email);
        return storedCode != null && storedCode.equals(inputCode);
    }
}
