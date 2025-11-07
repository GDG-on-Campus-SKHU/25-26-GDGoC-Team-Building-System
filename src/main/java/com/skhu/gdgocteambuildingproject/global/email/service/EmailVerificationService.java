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
        boolean isValid = storedCode != null && storedCode.equals(inputCode);

        if (isValid) {
            // 인증 성공 시 인증 완료 상태 저장
            markVerified(email);
            // 사용된 인증번호는 삭제 (보안상)
            redisTemplate.delete(email);
        }

        return isValid;
    }

    // 인증 완료 상태 저장 (10분 유지)
    public void markVerified(String email) {
        redisTemplate.opsForValue().set("verified:" + email, "true", 10, TimeUnit.MINUTES);
    }

    // 인증 완료 여부 확인
    public boolean isVerified(String email) {
        String verified = redisTemplate.opsForValue().get("verified:" + email);
        return "true".equals(verified);
    }

    // 인증 완료 상태 삭제 (비밀번호 재설정 후 보안상 제거)
    public void deleteVerifiedStatus(String email) {
        redisTemplate.delete("verified:" + email);
    }
}
