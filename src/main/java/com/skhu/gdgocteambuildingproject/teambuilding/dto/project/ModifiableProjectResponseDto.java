package com.skhu.gdgocteambuildingproject.teambuilding.dto.project;

import java.util.List;
import lombok.Builder;

@Builder
public record ModifiableProjectResponseDto(
        Long projectId,
        String projectName,
        int maxMemberCount,
        List<String> topics,
        List<ProjectAvailablePartResponseDto> availableParts,
        List<ProjectScheduleResponseDto> schedules,
        List<ProjectParticipantResponseDto> participants
) {
}
