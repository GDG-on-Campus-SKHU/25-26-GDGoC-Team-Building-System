package com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment;

import lombok.Builder;

@Builder
public record IdeaEnrollmentAvailabilityResponseDto(
        boolean canEnroll
) {
}

