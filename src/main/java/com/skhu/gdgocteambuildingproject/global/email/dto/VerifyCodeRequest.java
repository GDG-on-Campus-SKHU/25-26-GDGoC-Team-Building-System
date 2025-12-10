package com.skhu.gdgocteambuildingproject.global.email.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VerifyCodeRequest {
    private String email;
    private String code;

    @Builder
    public VerifyCodeRequest(String email, String code) {
        this.email = email;
        this.code = code;
    }
}
