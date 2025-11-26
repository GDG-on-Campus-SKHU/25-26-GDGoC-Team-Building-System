package com.skhu.gdgocteambuildingproject.global.email.controller;

import com.skhu.gdgocteambuildingproject.global.email.service.EmailService;
import com.skhu.gdgocteambuildingproject.global.email.service.EmailVerificationService;
import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import com.skhu.gdgocteambuildingproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final EmailVerificationService emailVerificationService;
    private final UserRepository userRepository;

    @PostMapping("/send")
    public ResponseEntity<String> sendCode(@RequestParam("email") String email) {

        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body("이메일을 입력해주세요.");
        }

        if (!userRepository.existsByEmailAndDeletedFalse(email)) {
            return ResponseEntity.badRequest().body(
                    ExceptionMessage.USER_EMAIL_NOT_EXIST.getMessage()
            );
        }

        String code = emailService.generateVerificationCode();
        emailVerificationService.saveCode(email, code);
        emailService.sendVerificationEmail(email, code);

        return ResponseEntity.ok("인증번호가 전송되었습니다.");
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyCode(@RequestParam("email") String email,
                                             @RequestParam("code") String code) {

        if (email == null || email.isBlank() || code == null || code.isBlank()) {
            return ResponseEntity.badRequest().body("이메일 또는 인증코드를 입력해주세요.");
        }

        boolean isValid = emailVerificationService.verifyCode(email, code);

        if (!isValid) {
            return ResponseEntity.badRequest().body("인증 실패 (코드가 틀렸거나 만료되었습니다.)");
        }

        return ResponseEntity.ok("인증 성공");
    }
}
