package com.skhu.gdgocteambuildingproject.global.email.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VerifyCodeRequest {
    private String email;
    private String code;
}
