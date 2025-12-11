package com.skhu.gdgocteambuildingproject.mypage.dto.request;

import com.skhu.gdgocteambuildingproject.user.domain.TechStack;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.TechStackType;
import lombok.Builder;

@Builder
public record UserTechStackUpdateRequestDto(
        TechStackType techStackType
) {
    public static UserTechStackUpdateRequestDto from(TechStack entity) {
        return UserTechStackUpdateRequestDto.builder()
                .techStackType(entity.getTechStackType())
                .build();
    }
}
