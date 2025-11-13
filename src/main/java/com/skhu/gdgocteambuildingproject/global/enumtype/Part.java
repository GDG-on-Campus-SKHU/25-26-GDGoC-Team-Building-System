package com.skhu.gdgocteambuildingproject.global.enumtype;

import lombok.Getter;

@Getter
public enum Part {
    PM("기획"),
    DESIGN("디자인"),
    WEB("프론트엔드(웹)"),
    MOBILE("프론트엔드(모바일)"),
    BACKEND("백엔드"),
    AI("AI/ML"),
    NULL("파트를 선택해주세요.");

    private final String koreanName;

    Part(String koreanName) {
        this.koreanName = koreanName;
    }
}
