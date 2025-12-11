package com.skhu.gdgocteambuildingproject.mypage.dto.request;

import com.skhu.gdgocteambuildingproject.user.domain.TechStack;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.TechStackType;
import lombok.Builder;

@Builder
public record TechStackUpdateRequestDto(
        TechStackType techStackType
) {
    public static TechStackUpdateRequestDto from(TechStack entity) {
        return TechStackUpdateRequestDto.builder()
                .techStackType(entity.getTechStackType())
                .build();
    }
}
