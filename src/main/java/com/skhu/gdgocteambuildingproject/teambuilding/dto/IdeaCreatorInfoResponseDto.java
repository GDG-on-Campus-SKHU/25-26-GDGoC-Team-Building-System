package com.skhu.gdgocteambuildingproject.teambuilding.dto;

import lombok.Builder;

@Builder
public record IdeaCreatorInfoResponseDto(
        String creatorName,
        String partName,
        String school
) {
}
