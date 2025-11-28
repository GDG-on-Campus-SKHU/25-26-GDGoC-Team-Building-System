package com.skhu.gdgocteambuildingproject.global.email.controller;

import com.skhu.gdgocteambuildingproject.global.email.service.ResetPasswordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/password")
@RequiredArgsConstructor
@Tag(name = "비밀번호 재설정 API", description = "이메일 인증 후 비밀번호 재설정")
public class ResetPasswordController {

    private final ResetPasswordService resetPasswordService;

    @PostMapping("/reset-password")
    @Operation(summary = "비밀번호 재설정")
    public ResponseEntity<String> resetPassword(
            @RequestParam String email,
            @RequestParam String newPassword
    ) {

        if (email.isBlank() || newPassword.isBlank()) {
            return ResponseEntity.badRequest().body("이메일과 새 비밀번호를 입력해주세요.");
        }

        resetPasswordService.resetPassword(email, newPassword);

        return ResponseEntity.ok("비밀번호가 성공적으로 재설정되었습니다.");
    }
}
