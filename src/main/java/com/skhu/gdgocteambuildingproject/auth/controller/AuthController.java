package com.skhu.gdgocteambuildingproject.auth.controller;

import com.skhu.gdgocteambuildingproject.auth.dto.request.LoginRequestDto;
import com.skhu.gdgocteambuildingproject.auth.dto.request.SignUpRequestDto;
import com.skhu.gdgocteambuildingproject.auth.dto.response.LoginResponseDto;
import com.skhu.gdgocteambuildingproject.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Auth API",
        description = """
인증 관련 기능을 제공합니다.

사용 가능한 ENUM 값:

Generation: 22-23, 23-24, 24-25, 25-26
Part: PM, DESIGN, WEB, MOBILE, BACKEND, AI
UserPosition: MEMBER, CORE, ORGANIZER
UserRole: OTHERS, SKHU_MEMBER, SKHU_ADMIN
""")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입", responses = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공")
    })
    @PostMapping("/signup")
    public ResponseEntity<LoginResponseDto> signUp(
            @RequestBody SignUpRequestDto dto,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authService.signUp(dto, response));
    }

    @Operation(summary = "로그인", responses = {
            @ApiResponse(responseCode = "200", description = "로그인 성공")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody LoginRequestDto dto,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authService.login(dto, response));
    }

    @Operation(summary = "토큰 재발급 (RefreshToken rotate)", responses = {
            @ApiResponse(responseCode = "200", description = "재발급 성공")
    })
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authService.refresh(refreshToken, response));
    }

    @Operation(summary = "로그아웃", responses = {
            @ApiResponse(responseCode = "204", description = "로그아웃 성공")
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        authService.logout(refreshToken, response);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "회원 탈퇴", responses = {
            @ApiResponse(responseCode = "204", description = "탈퇴 성공")
    })
    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal Long userId,
            HttpServletResponse response
    ) {
        authService.delete(userId, response);
        return ResponseEntity.noContent().build();
    }
}
