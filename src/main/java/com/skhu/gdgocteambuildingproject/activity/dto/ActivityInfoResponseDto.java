package com.skhu.gdgocteambuildingproject.activity.dto;

import lombok.Builder;

@Builder
public record ActivityInfoResponseDto(
        String title,
        String speaker,
        String generation,
        String videoUrl,
        String thumbnailUrl
) {
}
