package com.skhu.gdgocteambuildingproject.mypage.dto.request;


import com.skhu.gdgocteambuildingproject.user.domain.TechStack;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.TechStackType;
import lombok.Builder;

@Builder
public record TechStackDto(
        TechStackType techStackType
) {
    public static TechStackDto toEntity(TechStack entity) {
        return TechStackDto.builder()
                .techStackType(entity.getTechStackType())
                .build();
    }
}

