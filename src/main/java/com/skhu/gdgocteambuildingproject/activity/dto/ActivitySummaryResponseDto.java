package com.skhu.gdgocteambuildingproject.activity.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ActivitySummaryResponseDto(
        Long categoryId,
        String categoryTitle,
        List<ActivityInfoResponseDto> activities
) {
}
