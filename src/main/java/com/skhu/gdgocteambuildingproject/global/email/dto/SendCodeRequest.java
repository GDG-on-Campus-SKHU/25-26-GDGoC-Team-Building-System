package com.skhu.gdgocteambuildingproject.global.email.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendCodeRequest {
    private String email;

    @Builder
    public SendCodeRequest(String email) {
        this.email = email;
    }
}
