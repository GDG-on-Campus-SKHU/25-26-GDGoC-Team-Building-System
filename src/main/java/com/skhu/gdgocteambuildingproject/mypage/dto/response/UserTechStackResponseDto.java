package com.skhu.gdgocteambuildingproject.mypage.dto.response;

import com.skhu.gdgocteambuildingproject.user.domain.TechStack;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.TechStackType;
import lombok.Builder;

@Builder
public record UserTechStackResponseDto(
        TechStackType techStackType,
        String iconUrl
) {
    public static UserTechStackResponseDto from(TechStack entity) {
        return UserTechStackResponseDto.builder()
                .techStackType(entity.getTechStackType())
                .iconUrl(entity.getTechStackType().getTechStackIconUrl())
                .build();
    }
}
