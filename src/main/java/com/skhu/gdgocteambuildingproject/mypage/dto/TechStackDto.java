package com.skhu.gdgocteambuildingproject.mypage.dto;

import com.skhu.gdgocteambuildingproject.user.domain.TechStack;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.TechStackType;
import lombok.Builder;

@Builder
public record TechStackDto(
        TechStackType techStackType,
        String iconUrl
) {
    public static TechStackDto from(TechStack entity) {
        return TechStackDto.builder()
                .techStackType(entity.getTechStackType())
                .iconUrl(entity.getTechStackType().getTechStackIconUrl())
                .build();
    }
}
