package com.skhu.gdgocteambuildingproject.global.email.controller;

import com.skhu.gdgocteambuildingproject.global.email.service.EmailService;
import com.skhu.gdgocteambuildingproject.global.email.service.EmailVerificationService;
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

    // 인증번호 발송 요청
    @PostMapping("/send")
    public ResponseEntity<String> sendCode(@RequestParam("email") String email) {
        //회원가입한 이메일인 경우
        if (!userRepository.existsByEmailAndDeletedFalse(email)) {
            return ResponseEntity.badRequest().body("등록되지 않은 이메일입니다.");
        }

        String code = emailService.generateVerificationCode();
        emailVerificationService.saveCode(email, code);
        emailService.sendVerificationEmail(email, code);

        return ResponseEntity.ok("인증번호가 전송되었습니다.");
    }

    // 인증번호 확인 요청
    @PostMapping("/verify")
    public ResponseEntity<String> verifyCode(@RequestParam("email") String email,
                                             @RequestParam("code") String code) {
        boolean isValid = emailVerificationService.verifyCode(email, code);
        if (isValid) {
            return ResponseEntity.ok("인증 성공");
        } else {
            return ResponseEntity.badRequest().body("인증 실패");
        }
    }
}
