package com.skhu.gdgocteambuildingproject.auth.controller;

import com.skhu.gdgocteambuildingproject.auth.api.AuthControllerApi;
import com.skhu.gdgocteambuildingproject.auth.dto.request.LoginRequestDto;
import com.skhu.gdgocteambuildingproject.auth.dto.request.SignUpRequestDto;
import com.skhu.gdgocteambuildingproject.auth.dto.response.LoginResponseDto;
import com.skhu.gdgocteambuildingproject.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthControllerApi {

    private final AuthService authService;

    @Override
    public ResponseEntity<LoginResponseDto> signUp(
            SignUpRequestDto dto,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authService.signUp(dto, response));
    }

    @Override
    public ResponseEntity<LoginResponseDto> login(
            LoginRequestDto dto,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authService.login(dto, response));
    }

    @Override
    public ResponseEntity<LoginResponseDto> refresh(
            String refreshToken,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authService.refresh(refreshToken, response));
    }

    @Override
    public ResponseEntity<Void> logout(
            String refreshToken,
            HttpServletResponse response
    ) {
        authService.logout(refreshToken, response);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> delete(
            Long userId,
            HttpServletResponse response
    ) {
        authService.delete(userId, response);
        return ResponseEntity.noContent().build();
    }
}
