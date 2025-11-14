package com.skhu.gdgocteambuildingproject.teambuilding.dto.response;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import lombok.Builder;

@Builder
public record IdeaMemberCompositionResponseDto(
        Part part,
        int maxCount,
        int currentCount
) {
}
