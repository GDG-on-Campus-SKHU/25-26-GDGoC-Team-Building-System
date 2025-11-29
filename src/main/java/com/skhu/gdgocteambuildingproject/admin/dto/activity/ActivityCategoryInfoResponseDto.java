package com.skhu.gdgocteambuildingproject.admin.dto.activity;

import lombok.Builder;

@Builder
public record ActivityCategoryInfoResponseDto(
        Long categoryId,
        Long count,
        String categoryName,
        boolean isPublished
) {
}
