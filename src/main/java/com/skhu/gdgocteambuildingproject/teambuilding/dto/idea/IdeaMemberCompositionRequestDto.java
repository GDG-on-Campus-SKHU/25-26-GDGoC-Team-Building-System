package com.skhu.gdgocteambuildingproject.teambuilding.dto.idea;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import jakarta.validation.constraints.NotNull;

public record IdeaMemberCompositionRequestDto(
        @NotNull(message = "파트는 필수입니다.")
        Part part,
        @NotNull(message = "최대 인원수는 필수입니다.")
        Integer maxCount
) {
}
