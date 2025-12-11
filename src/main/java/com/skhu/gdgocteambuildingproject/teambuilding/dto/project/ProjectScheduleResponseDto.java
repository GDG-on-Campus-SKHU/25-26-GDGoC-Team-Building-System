package com.skhu.gdgocteambuildingproject.teambuilding.dto.project;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.ScheduleType;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ProjectScheduleResponseDto(
        ScheduleType scheduleType,
        LocalDateTime startAt,
        LocalDateTime endAt
) {
}
