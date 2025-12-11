package com.skhu.gdgocteambuildingproject.mypage.dto.response;

import com.skhu.gdgocteambuildingproject.user.domain.enumtype.TechStackType;

public record TechStackOptionResponseDto(
        String code,
        String displayName,
        String iconUrl
) {
    public static TechStackOptionResponseDto from(TechStackType type) {
        return new TechStackOptionResponseDto(
                type.name(),
                type.getDisplayName(),
                type.getTechStackIconUrl()
        );
    }
}
