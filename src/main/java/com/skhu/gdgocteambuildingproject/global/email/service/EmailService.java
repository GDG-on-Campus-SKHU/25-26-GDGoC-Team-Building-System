package com.skhu.gdgocteambuildingproject.global.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    // 인증번호 생성
    public String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10)); // 0~9 랜덤 숫자
        }
        return code.toString();
    }

    // 이메일 발송
    public void sendVerificationEmail(String to, String code) {
        String subject = "[GDG Team Project] 비밀번호 재설정 인증번호";
        String content = """
                <div style='font-family: Arial;'>
                    <h3>비밀번호 재설정을 위한 인증번호입니다.</h3>
                    <p>아래 인증번호를 입력해주세요:</p>
                    <h2 style='color: #4B9EEA;'>%s</h2>
                    <p>인증번호는 5분간 유효합니다.</p>
                </div>
                """.formatted(code);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("이메일 전송에 실패했습니다: " + e.getMessage());
        }
    }
}