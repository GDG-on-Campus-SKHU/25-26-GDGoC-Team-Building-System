package com.skhu.gdgocteambuildingproject.teambuilding.dto;

import lombok.Builder;

@Builder
public record IdeaMemberCompositionResponseDto(
        String partName,
        int maxCount,
        int currentCount
) {
}
