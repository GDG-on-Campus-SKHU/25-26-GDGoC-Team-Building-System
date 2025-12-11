package com.skhu.gdgocteambuildingproject.admin.dto.dashboard;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProjectSummaryResponseDto(
        Long id,
        String projectName,
        int ideaCount,
        int currentParticipants,
        int maxMemberCount,
        String currentScheduleType,
        LocalDateTime currentScheduleDeadline
) {
}
