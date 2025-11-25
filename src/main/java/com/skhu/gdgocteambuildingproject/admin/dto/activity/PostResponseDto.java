package com.skhu.gdgocteambuildingproject.admin.dto.activity;

import lombok.Builder;

@Builder
public record PostResponseDto(
    Long id,
    String title,
    String speaker,
    String generation,
    String videoUrl,
    String thumbnailUrl
) {
}
