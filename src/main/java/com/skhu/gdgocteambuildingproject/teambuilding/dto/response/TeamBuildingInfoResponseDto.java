package com.skhu.gdgocteambuildingproject.teambuilding.dto.response;

import com.skhu.gdgocteambuildingproject.admin.dto.project.ProjectTotalResponseDto;
import lombok.Builder;

@Builder
public record TeamBuildingInfoResponseDto(
        ProjectTotalResponseDto project,
        boolean registrable,
        boolean canEnroll
) {
}
