package com.skhu.gdgocteambuildingproject.auth.dto.request;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.Generation;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserPosition;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignUpRequestDto {

    @Schema(example = "홍길동", description = "사용자 이름")
    @NotBlank
    private String name;

    @Schema(example = "test@example.com", description = "사용자 이메일")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @Schema(example = "test1234!", description = "비밀번호 (8자 이상 + 특수문자)")
    @NotBlank
    private String password;

    @Schema(example = "test1234!", description = "비밀번호 확인")
    @NotBlank
    private String passwordConfirm;

    @Schema(example = "01012345678", description = "전화번호")
    @NotBlank
    private String number;

    @Schema(example = "성공회대학교", description = "학교명")
    @NotBlank
    private String school;

    @Schema(
            example = "GEN_24_25",
            description = """
                    기수(Generation) Enum 값:
                    - GEN_22_23
                    - GEN_23_24
                    - GEN_24_25
                    - GEN_25_26
                    """
    )
    private Generation generation;

    @Schema(
            example = "WEB",
            description = """
                    파트(Part) Enum 값 예시:
                    - WEB
                    - APP
                    - AI
                    - DESIGN
                    """
    )
    private Part part;

    @Schema(
            example = "MEMBER",
            description = """
                    사용자 포지션(UserPosition) Enum 값:
                    - MEMBER
                    - CORE
                    - ORGANIZER
                    """
    )
    private UserPosition position;

    @Schema(
            example = "OTHERS",
            description = """
                    사용자 권한(UserRole) Enum 값:
                    - OTHERS
                    - SKHU_MEMBER
                    - SKHU_ADMIN
                    - BANNED
                    """
    )

    private UserRole role;

    public User toEntity(String encodedPassword) {
        return User.builder()
                .email(this.email)
                .password(encodedPassword)
                .name(this.name)
                .number(this.number)
                .school(this.school)
                .role(this.role)
                .part(this.part)
                .build();
    }
}
