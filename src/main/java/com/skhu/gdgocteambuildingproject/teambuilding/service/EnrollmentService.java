package com.skhu.gdgocteambuildingproject.teambuilding.service;

import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.EnrollmentAvailabilityResponseDto;

public interface EnrollmentService {
    EnrollmentAvailabilityResponseDto getAvailabilityInfo(
            long ideaId,
            long applicantId
    );
}
