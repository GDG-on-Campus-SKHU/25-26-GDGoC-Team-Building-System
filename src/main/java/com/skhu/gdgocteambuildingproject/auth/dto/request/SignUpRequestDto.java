package com.skhu.gdgocteambuildingproject.auth.dto.request;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
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

    @Schema(example = "24-25", description = "기수(Generation, label 값 사용)")
    private String generation;

    @Schema(example = "WEB", description = "파트(Part)")
    private Part part;

    @Schema(example = "MEMBER", description = "사용자 포지션(UserPosition)")
    private UserPosition position;

    @Schema(example = "OTHERS", description = "사용자 권한(UserRole)")
    private UserRole role;
}
