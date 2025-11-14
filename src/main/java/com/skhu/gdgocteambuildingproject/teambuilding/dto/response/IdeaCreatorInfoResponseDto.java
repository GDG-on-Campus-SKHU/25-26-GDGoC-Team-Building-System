package com.skhu.gdgocteambuildingproject.teambuilding.dto.response;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import lombok.Builder;

@Builder
public record IdeaCreatorInfoResponseDto(
        String creatorName,
        Part part,
        String school
) {
}
