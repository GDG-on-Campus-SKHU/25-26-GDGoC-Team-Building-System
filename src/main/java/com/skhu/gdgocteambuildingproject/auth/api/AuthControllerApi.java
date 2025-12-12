package com.skhu.gdgocteambuildingproject.auth.api;

import com.skhu.gdgocteambuildingproject.auth.dto.request.LoginRequestDto;
import com.skhu.gdgocteambuildingproject.auth.dto.request.SignUpRequestDto;
import com.skhu.gdgocteambuildingproject.auth.dto.response.LoginResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Auth API",
        description = """
- Generation: 22-23, 23-24, 24-25, 25-26
- Part: PM, DESIGN, WEB, MOBILE, BACKEND, AI
- UserPosition: MEMBER, CORE, ORGANIZER
- UserRole: ROLE_SKHU_ADMIN(관리자), ROLE_SKHU_MEMBER(회원), ROLE_OTHERS(외부 사용자)
"""
)
@RequestMapping("/auth")
public interface AuthControllerApi {

    @Operation(
            summary = "회원가입",
            description = "사용자 정보를 입력받아 회원가입을 진행하고, 로그인 토큰을 발급합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원가입 성공"),
                    @ApiResponse(responseCode = "400", description = """
                잘못된 요청
                - 입력값 검증 실패 (이름 길이, 이메일 형식, 비밀번호 규칙 등)
                - 이메일 또는 전화번호 중복
                - 유효하지 않은 Part / UserPosition / UserRole
                - 관리자 권한으로 회원가입 시도
                """),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    @PostMapping("/signup")
    ResponseEntity<LoginResponseDto> signUp(
            @Valid @RequestBody SignUpRequestDto dto,
            HttpServletResponse response
    );

    @Operation(
            summary = "로그인",
            description = "이메일과 비밀번호로 로그인하고 토큰을 발급합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공")
            }
    )
    @PostMapping("/login")
    ResponseEntity<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto dto,
            HttpServletResponse response
    );

    @Operation(
            summary = "토큰 재발급",
            description = "Refresh Token을 이용해 Access Token을 재발급합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "토큰 재발급 성공")
            }
    )
    @PostMapping("/refresh")
    ResponseEntity<LoginResponseDto> refresh(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    );

    @Operation(
            summary = "로그아웃",
            description = "Refresh Token을 만료시켜 로그아웃 처리합니다.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "로그아웃 성공")
            }
    )
    @PostMapping("/logout")
    ResponseEntity<Void> logout(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    );

    @Operation(
            summary = "회원 탈퇴",
            description = "현재 로그인된 사용자를 탈퇴 처리합니다.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "회원 탈퇴 성공")
            }
    )
    @DeleteMapping("/delete")
    ResponseEntity<Void> delete(
            @AuthenticationPrincipal Long userId,
            HttpServletResponse response
    );
}
