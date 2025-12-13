package com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.ScheduleType;
import lombok.Builder;

@Builder
public record EnrollmentReadableResponseDto(
        ScheduleType scheduleType,
        boolean readable
) {
}
