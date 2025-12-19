package com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment;

import lombok.Builder;

@Builder
public record EnrollmentAvailabilitySimpleResponseDto(
        boolean canEnroll,
        boolean isCreator
) {
}

