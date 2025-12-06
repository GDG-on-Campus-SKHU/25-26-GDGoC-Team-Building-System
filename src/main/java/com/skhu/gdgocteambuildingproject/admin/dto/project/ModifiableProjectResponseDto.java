package com.skhu.gdgocteambuildingproject.admin.dto.project;

import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.ProjectScheduleResponseDto;
import java.util.List;
import lombok.Builder;

@Builder
public record ModifiableProjectResponseDto(
        Long projectId,
        String projectName,
        int maxMemberCount,
        List<ProjectAvailablePartResponseDto> availableParts,
        List<ProjectScheduleResponseDto> schedules,
        List<Long> participatedUserIds
) {
}
