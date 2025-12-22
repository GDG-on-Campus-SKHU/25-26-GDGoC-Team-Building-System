package com.skhu.gdgocteambuildingproject.auth.service;

import com.skhu.gdgocteambuildingproject.auth.dto.request.LoginRequestDto;
import com.skhu.gdgocteambuildingproject.auth.dto.request.SignUpRequestDto;
import com.skhu.gdgocteambuildingproject.auth.dto.response.LoginResponseDto;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    void signUp(SignUpRequestDto dto);

    LoginResponseDto login(LoginRequestDto dto, HttpServletResponse response);

    LoginResponseDto refresh(String refreshToken, HttpServletResponse response);

    void logout(String refreshToken, HttpServletResponse response);

    void delete(Long userId, HttpServletResponse response);
}
