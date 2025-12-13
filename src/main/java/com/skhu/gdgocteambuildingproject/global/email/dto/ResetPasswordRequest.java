package com.skhu.gdgocteambuildingproject.global.email.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResetPasswordRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @Schema(
            example = "test@example.com",
            description = "비밀번호를 재설정할 사용자 이메일"
    )
    private String email;

    @NotBlank(message = "인증 코드는 필수입니다.")
    @Schema(
            example = "123456",
            description = "이메일로 발송된 6자리 인증 코드"
    )
    private String code;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(
            regexp = "^(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$",
            message = "비밀번호는 8자 이상이며 특수문자를 1개 이상 포함해야 합니다."
    )
    @Schema(
            example = "newPassword!1",
            description = "새 비밀번호 (8자 이상 + 특수문자 포함)"
    )
    private String newPassword;
}
