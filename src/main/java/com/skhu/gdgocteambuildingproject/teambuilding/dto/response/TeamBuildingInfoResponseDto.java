package com.skhu.gdgocteambuildingproject.teambuilding.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record TeamBuildingInfoResponseDto(
        Long projectId,
        String projectName,
        int maxMemberCount,
        boolean registrable,
        List<ProjectScheduleResponseDto> schedules
) {
}
