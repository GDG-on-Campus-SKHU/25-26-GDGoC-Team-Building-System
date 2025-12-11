package com.skhu.gdgocteambuildingproject.teambuilding.dto.project;

import lombok.Builder;

@Builder
public record TeamBuildingInfoResponseDto(
        ProjectTotalResponseDto project,
        boolean registrable,
        boolean canEnroll
) {
}
