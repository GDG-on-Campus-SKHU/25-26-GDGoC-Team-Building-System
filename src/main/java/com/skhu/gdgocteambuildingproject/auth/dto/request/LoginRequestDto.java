package com.skhu.gdgocteambuildingproject.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequestDto {

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank
    private String password;
}