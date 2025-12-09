package com.skhu.gdgocteambuildingproject.auth.service;

import com.skhu.gdgocteambuildingproject.auth.dto.request.LoginRequestDto;
import com.skhu.gdgocteambuildingproject.auth.dto.request.SignUpRequestDto;
import com.skhu.gdgocteambuildingproject.auth.dto.token.AuthTokenBundle;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    AuthTokenBundle signUp(SignUpRequestDto dto, HttpServletResponse response);
    AuthTokenBundle login(LoginRequestDto dto, HttpServletResponse response);
    AuthTokenBundle refresh(String refreshToken, HttpServletResponse response);
    void logout(String refreshToken, HttpServletResponse response);
    void delete(Long userId, HttpServletResponse response);
}
