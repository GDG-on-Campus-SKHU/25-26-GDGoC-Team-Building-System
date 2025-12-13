package com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment;

import java.util.List;
import lombok.Builder;

@Builder
public record SentEnrollmentsResponseDto(
        boolean scheduleEnded,
        List<SentEnrollmentResponseDto> enrollments
) {
}

