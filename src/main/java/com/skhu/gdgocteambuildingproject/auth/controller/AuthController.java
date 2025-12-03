package com.skhu.gdgocteambuildingproject.auth.controller;

import com.skhu.gdgocteambuildingproject.auth.dto.request.LoginRequestDto;
import com.skhu.gdgocteambuildingproject.auth.dto.request.RefreshTokenRequestDto;
import com.skhu.gdgocteambuildingproject.auth.dto.request.SignUpRequestDto;
import com.skhu.gdgocteambuildingproject.auth.dto.response.LoginResponseDto;
import com.skhu.gdgocteambuildingproject.auth.service.AuthService;
import com.skhu.gdgocteambuildingproject.global.jwt.service.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth API",
        description = "사용자 인증(로그인/회원가입) 및 JWT 기반 토큰 발급·갱신·삭제 기능을 담당")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "회원가입",
            description = "새로운 사용자를 등록하고, Access Token과 Refresh Token을 발급한다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = LoginResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력값 또는 이미 존재하는 이메일"),
    })
    @PostMapping("/signup")
    public ResponseEntity<LoginResponseDto> signUp(@RequestBody @Valid SignUpRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signUp(dto));
    }

    @Operation(
            summary = "로그인",
            description = "이메일과 비밀번호를 입력하여 로그인하고, Access Token과 Refresh Token을 발급한다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = LoginResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 이메일 또는 비밀번호 불일치"),
            @ApiResponse(responseCode = "409", description = "삭제된 회원 또는 승인 대기 중인 사용자"),
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @Operation(
            summary = "토큰 재발급",
            description = "유효한 Refresh Token을 기반으로 Access Token 및 새로운 Refresh Token을 재발급한다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공",
                    content = @Content(schema = @Schema(implementation = LoginResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 Refresh Token"),
            @ApiResponse(responseCode = "409", description = "탈퇴 회원 또는 승인 대기 중인 사용자"),
    })
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(@RequestBody @Valid RefreshTokenRequestDto dto) {
        return ResponseEntity.ok(authService.refresh(dto));
    }

    @Operation(
            summary = "로그아웃",
            description = "해당 Refresh Token을 삭제하여 로그아웃 처리한다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "로그아웃 성공"),
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody RefreshTokenRequestDto dto) {
        authService.logout(dto);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "회원 탈퇴",
            description = "현재 로그인한 사용자를 소프트 삭제 처리한다. 탈퇴 시점(deletedAt)도 저장됨."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "회원 탈퇴 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
    })
    @DeleteMapping
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        authService.delete(userPrincipal.getUser().getId());
        return ResponseEntity.noContent().build();
    }
}
