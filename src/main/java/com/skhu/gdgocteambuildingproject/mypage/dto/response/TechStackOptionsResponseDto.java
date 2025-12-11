package com.skhu.gdgocteambuildingproject.mypage.dto.response;

import com.skhu.gdgocteambuildingproject.user.domain.enumtype.TechStackType;

public record TechStackOptionsResponseDto(
        String code,
        String displayName,
        String iconUrl
) {
    public static TechStackOptionsResponseDto from(TechStackType type) {
        return new TechStackOptionsResponseDto(
                type.name(),
                type.getDisplayName(),
                type.getTechStackIconUrl()
        );
    }
}
