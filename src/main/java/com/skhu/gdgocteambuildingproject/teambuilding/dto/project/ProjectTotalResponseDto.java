package com.skhu.gdgocteambuildingproject.teambuilding.dto.project;

import java.util.List;
import lombok.Builder;

@Builder
public record ProjectTotalResponseDto(
        Long projectId,
        String projectName,
        int maxMemberCount,
        List<ProjectAvailablePartResponseDto> availableParts,
        List<ProjectScheduleResponseDto> schedules
) {
}

