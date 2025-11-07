package com.skhu.gdgocteambuildingproject.global.email.controller;

import com.skhu.gdgocteambuildingproject.global.email.service.EmailVerificationService;
import com.skhu.gdgocteambuildingproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/password")
@RequiredArgsConstructor
public class ResetPasswordController {

    private final EmailVerificationService emailVerificationService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam("email") String email,
                                                @RequestParam("newPassword") String newPassword) {
        if (!emailVerificationService.isVerified(email)) {
            return ResponseEntity.badRequest().body("이메일 인증이 완료되지 않았습니다.");
        }

        var user = userRepository.findByEmail(email).get();
        user.updatePassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        emailVerificationService.deleteVerifiedStatus(email);
        return ResponseEntity.ok("비밀번호가 성공적으로 재설정되었습니다.");
    }
}