package com.skhu.gdgocteambuildingproject.admin.dto.activity;

import lombok.Builder;

@Builder
public record ActivityResponseDto(
        Long categoryId,
        String categoryTitle,
        Long postId,
        String postTitle,
        String speaker,
        String generation,
        String videoUrl,
        String thumbnailUrl
) {
}
