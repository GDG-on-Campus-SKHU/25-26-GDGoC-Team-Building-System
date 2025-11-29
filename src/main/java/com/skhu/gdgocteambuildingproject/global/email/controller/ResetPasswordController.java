package com.skhu.gdgocteambuildingproject.global.email.controller;

import com.skhu.gdgocteambuildingproject.global.email.exception.EmailNotVerifiedException;
import com.skhu.gdgocteambuildingproject.global.email.service.EmailVerificationService;
import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import com.skhu.gdgocteambuildingproject.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "비밀번호 재설정 API", description = "이메일 인증 후 비밀번호를 재설정하는 API")
public class ResetPasswordController {

    private final EmailVerificationService emailVerificationService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/reset-password")
    @Operation(
            summary = "비밀번호 재설정",
            description = "이메일 인증이 완료된 사용자의 비밀번호를 새로운 비밀번호로 재설정합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "비밀번호 재설정 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 오류 또는 이메일 인증 미완료"),
    })
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
