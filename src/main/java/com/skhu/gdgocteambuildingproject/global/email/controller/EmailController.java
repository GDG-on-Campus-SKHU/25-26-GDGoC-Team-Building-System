package com.skhu.gdgocteambuildingproject.global.email.controller;

import com.skhu.gdgocteambuildingproject.global.email.domain.EmailVerification;
import com.skhu.gdgocteambuildingproject.global.email.repository.EmailVerificationRepository;
import com.skhu.gdgocteambuildingproject.global.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final EmailVerificationRepository repository;

    // 인증번호 요청
    @PostMapping("/send")
    @Transactional
    public ResponseEntity<String> sendCode(@RequestParam String email) {
        String code = emailService.generateVerificationCode();

        repository.findByEmail(email).ifPresentOrElse(
                ev -> ev = new EmailVerification(email, code),
                () -> repository.save(new EmailVerification(email, code))
        );

        emailService.sendVerificationEmail(email, code);
        return ResponseEntity.ok("인증번호가 이메일로 발송되었습니다.");
    }

    // 인증번호 확인
    @PostMapping("/verify")
    @Transactional
    public ResponseEntity<String> verifyCode(@RequestParam String email, @RequestParam String code) {
        EmailVerification ev = repository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일로 요청된 인증이 없습니다."));

        if (!ev.getCode().equals(code)) {
            return ResponseEntity.badRequest().body("인증번호가 일치하지 않습니다.");
        }

        ev.markVerified();
        return ResponseEntity.ok("이메일 인증이 완료되었습니다.");
    }
}