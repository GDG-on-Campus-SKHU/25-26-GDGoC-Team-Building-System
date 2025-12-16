package com.skhu.gdgocteambuildingproject.admin.dto.activity;

import lombok.Builder;

import java.util.List;

@Builder
public record ActivityResponseDto(
        Long categoryId,
        String categoryTitle,
        boolean publish,
        List<PostResponseDto> posts
) {
}
