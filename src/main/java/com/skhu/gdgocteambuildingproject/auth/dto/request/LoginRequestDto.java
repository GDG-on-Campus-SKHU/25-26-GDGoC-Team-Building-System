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

    @Schema(example = "test1234!")
    private String password;
}
