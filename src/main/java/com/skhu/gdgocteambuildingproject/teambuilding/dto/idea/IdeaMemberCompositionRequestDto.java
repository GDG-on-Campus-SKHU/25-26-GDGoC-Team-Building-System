package com.skhu.gdgocteambuildingproject.teambuilding.dto.idea;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;

public record IdeaMemberCompositionRequestDto(
        Part part,
        int maxCount
) {
}
