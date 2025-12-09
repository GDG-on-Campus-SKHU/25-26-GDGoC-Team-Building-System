package com.skhu.gdgocteambuildingproject.auth.dto.token;

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

    public static AuthTokenBundle of(String accessToken, String refreshToken, User user) {
        return AuthTokenBundle.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(user)
                .build();
    }

    public LoginResponseDto toLoginResponse() {
        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .build();
    }
}
