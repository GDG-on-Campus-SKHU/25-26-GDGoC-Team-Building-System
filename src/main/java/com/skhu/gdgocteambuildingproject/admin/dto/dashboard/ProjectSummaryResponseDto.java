package com.skhu.gdgocteambuildingproject.admin.dto.dashboard;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.ScheduleType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProjectSummaryResponseDto(
        Long id,
        String projectName,
        int ideaCount,
        int currentParticipants,
        int maxMemberCount,
        ScheduleType currentScheduleType,
        LocalDateTime currentScheduleDeadline
) {
}
