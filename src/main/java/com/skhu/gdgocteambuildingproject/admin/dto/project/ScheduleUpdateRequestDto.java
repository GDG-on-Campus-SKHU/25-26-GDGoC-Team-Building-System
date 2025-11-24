package com.skhu.gdgocteambuildingproject.admin.dto.project;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.ScheduleType;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record ScheduleUpdateRequestDto(
        @NotNull
        ScheduleType scheduleType,
        @NotNull
        LocalDateTime startAt,
        @NotNull
        LocalDateTime endAt
) {
}
