package com.skhu.gdgocteambuildingproject.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenPairDto {
    private String accessToken;
    private String refreshToken;
}
