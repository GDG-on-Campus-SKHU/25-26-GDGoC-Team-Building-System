package com.skhu.gdgocteambuildingproject.auth.dto;

import com.skhu.gdgocteambuildingproject.user.domain.User;
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

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$", message = "비밀번호는 8자 이상이며 특수문자를 포함해야 합니다.")
    private String password;

    @NotBlank
    private String passwordConfirm;

    @NotBlank
    private String number;

    private String introduction;

    @NotBlank
    private String school;

    private String generation;
    private String part;

    private UserPosition position; // MEMBER / CORE / ORGANIZER
    private UserRole role;         // SKHU_MEMBER / OTHERS

    public User toEntity(String encodedPassword) {
        return User.builder()
                .email(this.email)
                .password(encodedPassword)
                .name(this.name)
                .number(this.number)
                .introduction(this.introduction)
                .school(this.school)
                .role(this.role)
                .position(this.position)
                .part(this.part)
                .generation(this.generation)
                .build();
    }
}