package com.skhu.gdgocteambuildingproject.constants.dto;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;

public record PartResponseDto(
        String name,
        String koreanName
) {
    public static PartResponseDto from(Part part) {
        return new PartResponseDto(part.name(), part.getKoreanName());
    }
}

