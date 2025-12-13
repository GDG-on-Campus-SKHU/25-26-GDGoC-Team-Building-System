package com.skhu.gdgocteambuildingproject.global.email.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VerifyCodeRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @Schema(
            example = "test@example.com",
            description = "인증 요청한 이메일"
    )
    private String email;

    @NotBlank(message = "인증 코드는 필수입니다.")
    @Schema(
            example = "123456",
            description = "이메일로 받은 인증 코드"
    )
    private String code;
}
