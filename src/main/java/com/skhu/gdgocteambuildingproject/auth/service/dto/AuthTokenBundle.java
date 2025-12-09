package com.skhu.gdgocteambuildingproject.auth.service.dto;

import com.skhu.gdgocteambuildingproject.auth.dto.response.LoginResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AuthTokenBundle {
    private final String accessToken;
    private final String refreshToken;
    private final User user;

    public LoginResponseDto toLoginResponse() {
        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .build();
    }
}
