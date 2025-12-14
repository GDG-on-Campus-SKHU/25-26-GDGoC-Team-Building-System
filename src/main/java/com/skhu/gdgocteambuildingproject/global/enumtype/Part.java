package com.skhu.gdgocteambuildingproject.global.enumtype;

import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Part {
    PM("기획"),
    DESIGN("디자인"),
    WEB("프론트엔드(웹)"),
    MOBILE("프론트엔드(모바일)"),
    BACKEND("백엔드"),
    AI("AI/ML");

    private final String koreanName;

    Part(String koreanName) {
        this.koreanName = koreanName;
    }

    public static Part from(String value) {
        return Arrays.stream(values())
                .filter(part ->
                        part.name().equalsIgnoreCase(value)
                                || part.koreanName.equals(value)
                )
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                ExceptionMessage.INVALID_PART.getMessage()
                        )
                );
    }
}
