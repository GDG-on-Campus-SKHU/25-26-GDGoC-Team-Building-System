package com.skhu.gdgocteambuildingproject.mypage.dto.response;

import com.skhu.gdgocteambuildingproject.user.domain.TechStack;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.TechStackType;
import lombok.Builder;

@Builder
public record TechStackResponseDto(
        TechStackType techStackType,
        String iconUrl
) {
    public static TechStackResponseDto from(TechStack entity) {
        return TechStackResponseDto.builder()
                .techStackType(entity.getTechStackType())
                .iconUrl(entity.getTechStackType().getTechStackIconUrl())
                .build();
    }
}
