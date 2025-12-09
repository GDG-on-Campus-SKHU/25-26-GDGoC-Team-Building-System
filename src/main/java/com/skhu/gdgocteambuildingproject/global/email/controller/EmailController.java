package com.skhu.gdgocteambuildingproject.global.email.controller;

import com.skhu.gdgocteambuildingproject.global.email.dto.ResetPasswordRequest;
import com.skhu.gdgocteambuildingproject.global.email.dto.SendCodeRequest;
import com.skhu.gdgocteambuildingproject.global.email.dto.VerifyCodeRequest;
import com.skhu.gdgocteambuildingproject.global.email.service.EmailService;
import com.skhu.gdgocteambuildingproject.global.email.service.EmailVerificationService;
import com.skhu.gdgocteambuildingproject.global.email.service.ResetPasswordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
@Tag(name = "Email 인증 API", description = "이메일 인증번호 발송, 인증 검증, 비밀번호 재설정 API")
public class EmailController {

    private final EmailService emailService;
    private final EmailVerificationService emailVerificationService;
    private final ResetPasswordService resetPasswordService;

    @PostMapping("/send")
    @Operation(
            summary = "이메일 인증번호 전송",
            description = """
                    입력된 이메일로 6자리 인증번호를 전송합니다.
                    - 존재하는 회원 이메일인지 검증합니다.
                    - 인증번호는 Redis에 5분간 저장됩니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인증번호 전송 완료"),
            @ApiResponse(responseCode = "400", description = "이메일이 존재하지 않음 또는 형식 오류")
    })
    public ResponseEntity<String> sendCode(
            @RequestBody SendCodeRequest request
    ) {
        emailService.sendCode(request.getEmail());
        return ResponseEntity.ok("인증번호가 전송되었습니다.");
    }

    @PostMapping("/verify")
    @Operation(
            summary = "이메일 인증번호 확인",
            description = """
                    이메일로 전송된 인증번호가 Redis의 값과 일치하는지 검증합니다.
                    - 인증 성공 시 저장된 인증번호는 삭제됩니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인증 성공"),
            @ApiResponse(responseCode = "400", description = "코드 불일치, 만료 또는 입력 오류")
    })
    public ResponseEntity<String> verifyCode(
            @RequestBody VerifyCodeRequest request
    ) {
        emailVerificationService.verify(request.getEmail(), request.getCode());
        return ResponseEntity.ok("인증 성공");
    }

    @PostMapping("/reset-password")
    @Operation(
            summary = "비밀번호 재설정",
            description = """
                    인증번호 검증 후 새 비밀번호로 재설정합니다.
                    - 기존 비밀번호와 동일하면 오류가 발생합니다.
                    - 비밀번호는 최소 8자 이상이어야 합니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "비밀번호 재설정 성공"),
            @ApiResponse(responseCode = "400", description = "코드 불일치, 만료 또는 비밀번호 형식 오류")
    })
    public ResponseEntity<String> resetPassword(
            @RequestBody ResetPasswordRequest request
    ) {
        resetPasswordService.resetPassword(
                request.getEmail(),
                request.getCode(),
                request.getNewPassword()
        );

        return ResponseEntity.ok("비밀번호가 성공적으로 재설정되었습니다.");
    }
}
