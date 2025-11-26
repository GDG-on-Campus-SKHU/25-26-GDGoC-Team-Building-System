package com.skhu.gdgocteambuildingproject.global.email.controller;

import com.skhu.gdgocteambuildingproject.global.email.exception.EmailNotVerifiedException;
import com.skhu.gdgocteambuildingproject.global.email.service.EmailVerificationService;
import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
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

        if (email == null || email.isBlank() || newPassword == null || newPassword.isBlank()) {
            return ResponseEntity.badRequest().body("이메일과 새 비밀번호를 입력해주세요.");
        }

        if (!emailVerificationService.isVerified(email)) {
            throw new EmailNotVerifiedException(
                    ExceptionMessage.EMAIL_NOT_VERIFIED.getMessage()
            );
        }

        var user = userRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                ExceptionMessage.USER_EMAIL_NOT_EXIST.getMessage()
                        ));

        user.updatePassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        emailVerificationService.deleteVerifiedStatus(email);

        return ResponseEntity.ok("비밀번호가 성공적으로 재설정되었습니다.");
    }
}
