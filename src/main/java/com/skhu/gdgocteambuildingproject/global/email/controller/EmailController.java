package com.skhu.gdgocteambuildingproject.global.email.controller;

import com.skhu.gdgocteambuildingproject.global.email.api.EmailControllerApi;
import com.skhu.gdgocteambuildingproject.global.email.dto.ResetPasswordRequest;
import com.skhu.gdgocteambuildingproject.global.email.dto.SendCodeRequest;
import com.skhu.gdgocteambuildingproject.global.email.dto.VerifyCodeRequest;
import com.skhu.gdgocteambuildingproject.global.email.service.EmailService;
import com.skhu.gdgocteambuildingproject.global.email.service.EmailVerificationService;
import com.skhu.gdgocteambuildingproject.global.email.service.ResetPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController implements EmailControllerApi {

    private final EmailService emailService;
    private final EmailVerificationService emailVerificationService;
    private final ResetPasswordService resetPasswordService;

    @PostMapping("/send")
    @Override
    public ResponseEntity<Void> sendCode(SendCodeRequest request) {
        emailService.sendPasswordResetVerificationCode(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify")
    @Override
    public ResponseEntity<Void> verifyCode(VerifyCodeRequest request) {
        emailVerificationService.validateCode(
                request.getEmail(),
                request.getCode()
        );
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    @Override
    public ResponseEntity<Void> resetPassword(ResetPasswordRequest request) {
        resetPasswordService.resetPassword(
                request.getEmail(),
                request.getCode(),
                request.getNewPassword()
        );
        return ResponseEntity.noContent().build();
    }
}
