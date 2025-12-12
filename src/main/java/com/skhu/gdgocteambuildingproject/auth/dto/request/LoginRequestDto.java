package com.skhu.gdgocteambuildingproject.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class LoginRequestDto {

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @Schema(example = "test@example.com")
    private String email;

    @Pattern(
            regexp = "^(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$",
            message = "비밀번호는 8자 이상이며 특수문자를 1개 이상 포함해야 합니다."
    )
    @Schema(example = "test1234!")
    private String password;
}
