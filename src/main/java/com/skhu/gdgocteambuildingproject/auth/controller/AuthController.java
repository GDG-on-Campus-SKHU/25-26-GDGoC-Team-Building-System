package com.skhu.gdgocteambuildingproject.auth.controller;

import com.skhu.gdgocteambuildingproject.auth.api.AuthControllerApi;
import com.skhu.gdgocteambuildingproject.auth.dto.request.LoginRequestDto;
import com.skhu.gdgocteambuildingproject.auth.dto.request.SignUpRequestDto;
import com.skhu.gdgocteambuildingproject.auth.dto.response.LoginResponseDto;
import com.skhu.gdgocteambuildingproject.auth.service.AuthService;
import com.skhu.gdgocteambuildingproject.global.jwt.service.UserPrincipal;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<LoginResponseDto> reissueAccessToken(
            @CookieValue(name = "refreshToken") String refreshToken,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authService.refresh(refreshToken, response));
    }

    @Override
    public ResponseEntity<Void> logout(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        authService.logout(refreshToken, response);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> delete(HttpServletResponse response) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            throw new IllegalStateException("UNAUTHORIZED");
        }

        authService.delete(principal.getUser().getId(), response);
        return ResponseEntity.noContent().build();
    }
}
