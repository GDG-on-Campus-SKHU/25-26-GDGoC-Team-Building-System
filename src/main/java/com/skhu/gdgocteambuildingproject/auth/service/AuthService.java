package com.skhu.gdgocteambuildingproject.auth.service;

import com.skhu.gdgocteambuildingproject.auth.dto.request.LoginRequestDto;
import com.skhu.gdgocteambuildingproject.auth.dto.request.RefreshTokenRequestDto;
import com.skhu.gdgocteambuildingproject.auth.dto.request.SignUpRequestDto;
import com.skhu.gdgocteambuildingproject.auth.dto.response.LoginResponseDto;

public interface AuthService {

    LoginResponseDto signUp(SignUpRequestDto dto);

    LoginResponseDto login(LoginRequestDto dto);

    LoginResponseDto refresh(RefreshTokenRequestDto dto);

    void logout(RefreshTokenRequestDto dto);

    void delete(Long userId);
}
