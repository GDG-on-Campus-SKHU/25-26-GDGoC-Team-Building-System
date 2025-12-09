package com.skhu.gdgocteambuildingproject.auth.service;

import com.skhu.gdgocteambuildingproject.auth.dto.request.LoginRequestDto;
import com.skhu.gdgocteambuildingproject.auth.dto.request.SignUpRequestDto;
import com.skhu.gdgocteambuildingproject.auth.service.dto.AuthTokenBundle;

public interface AuthService {

    AuthTokenBundle signUp(SignUpRequestDto dto);

    AuthTokenBundle login(LoginRequestDto dto);

    AuthTokenBundle refresh(String refreshToken);

    void logout(String refreshToken);

    void delete(Long userId);
}
