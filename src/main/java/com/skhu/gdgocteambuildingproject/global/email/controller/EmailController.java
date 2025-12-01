package com.skhu.gdgocteambuildingproject.global.email.controller;

import com.skhu.gdgocteambuildingproject.global.email.service.EmailService;
import com.skhu.gdgocteambuildingproject.global.email.service.EmailVerificationService;
import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
@Tag(name = "Email 인증 API", description = "이메일 인증번호 전송 및 인증 검증 API")
public class EmailController {

    private final EmailService emailService;
    private final EmailVerificationService emailVerificationService;

    @PostMapping("/send")
    @Operation(
            summary = "이메일 인증번호 전송",
            description = "입력한 이메일로 6자리 인증번호를 전송합니다. 존재하는 회원의 이메일만 전송 가능합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인증번호 전송 완료"),
            @ApiResponse(responseCode = "400", description = "이메일이 존재하지 않음 또는 입력값 오류")
    })
    public ResponseEntity<String> sendCode(@RequestParam("email") String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(ExceptionMessage.EMAIL_INVALID_FORMAT.getMessage());
        }

        emailService.validateEmailExists(email);

        String code = emailService.generateVerificationCode();
        emailVerificationService.saveCode(email, code);
        emailService.sendVerificationEmail(email, code);

        return ResponseEntity.ok("인증번호가 전송되었습니다.");
    }

    @PostMapping("/verify")
    @Operation(
            summary = "이메일 인증번호 확인",
            description = "이메일로 전송된 인증번호가 일치하는지 검증합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인증 성공"),
            @ApiResponse(responseCode = "400", description = "인증 실패 또는 입력값 오류")
    })
    public ResponseEntity<String> verifyCode(@RequestParam("email") String email,
                                             @RequestParam("code") String code) {
        if (email == null || email.isBlank() || code == null || code.isBlank()) {
            throw new IllegalArgumentException(ExceptionMessage.EMAIL_INVALID_FORMAT.getMessage());
        }

        emailVerificationService.verifyCodeOrThrow(email, code);

        return ResponseEntity.ok("인증 성공");
    }
}
