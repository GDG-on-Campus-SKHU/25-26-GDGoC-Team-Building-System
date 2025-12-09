package com.skhu.gdgocteambuildingproject.auth.dto.request;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserPosition;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignUpRequestDto {

    @Schema(example = "홍길동", description = "사용자 이름 (2~5자)")
    @Size(min = 2, max = 5, message = "이름은 2~5자여야 합니다.")
    private String name;

    @Schema(example = "test@example.com", description = "사용자 이메일")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @Schema(example = "test1234!", description = "비밀번호 (8자 이상 + 특수문자 1개 이상)")
    @NotBlank
    @Pattern(
            regexp = "^(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$",
            message = "비밀번호는 8자 이상이며 특수문자를 1개 이상 포함해야 합니다."
    )
    private String password;

    @Schema(example = "test1234!", description = "비밀번호 확인 (password와 동일해야 함)")
    @NotBlank
    private String passwordConfirm;

    @Schema(example = "010-1234-5678", description = "전화번호 (하이픈 포함)")
    @NotBlank
    @Pattern(
            regexp = "^010-\\d{4}-\\d{4}$",
            message = "전화번호는 010-1234-5678 형식이어야 합니다."
    )
    private String number;

    @Schema(example = "성공회대학교", description = "학교명 (자동 저장 또는 기본값 적용 가능)")
    @NotBlank
    private String school;

    @Schema(example = "24-25", description = "기수(Generation, 22-23 ~ 26-27)")
    private String generation;

    @Schema(example = "WEB", description = "사용자 파트(Part)")
    private Part part;

    @Schema(example = "MEMBER", description = "사용자 포지션(UserPosition): MEMBER / CORE / LEAD")
    private UserPosition position;

    @Schema(example = "OTHERS", description = "사용자 권한(UserRole)")
    private UserRole role;
}
