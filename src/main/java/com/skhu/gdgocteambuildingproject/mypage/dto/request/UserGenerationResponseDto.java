package com.skhu.gdgocteambuildingproject.mypage.dto.request;

import lombok.Builder;

@Builder
public record UserGenerationResponseDto(
        Long id,
        String generation,
        String position,
        boolean isMain
) {
}

