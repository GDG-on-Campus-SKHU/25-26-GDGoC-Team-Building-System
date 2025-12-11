package com.skhu.gdgocteambuildingproject.admin.dto.project;

import com.skhu.gdgocteambuildingproject.teambuilding.dto.project.ProjectScheduleResponseDto;
import java.util.List;
import lombok.Builder;

@Builder
public record ModifiableProjectResponseDto(
        Long projectId,
        String projectName,
        int maxMemberCount,
        List<ProjectAvailablePartResponseDto> availableParts,
        List<ProjectScheduleResponseDto> schedules,
        List<ProjectParticipantResponseDto> participants
) {
}
