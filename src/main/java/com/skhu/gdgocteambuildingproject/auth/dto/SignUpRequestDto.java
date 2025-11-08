package com.skhu.gdgocteambuildingproject.auth.dto;

import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserPosition;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignUpRequestDto {

    @NotBlank
    private String name;

    @NotBlank
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$", message = "비밀번호는 8자 이상이며 특수문자를 포함해야 합니다.")
    private String password;

    @NotBlank
    private String passwordConfirm;

    @NotBlank
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "전화번호는 010-1234-5678 형식으로 입력해야 합니다.")
    private String number;

    private String school;

    private String generation;
    private String part;

    private UserPosition position; // MEMBER / CORE / ORGANIZER
    private UserRole role;         // SKHU_MEMBER / OTHERS
}