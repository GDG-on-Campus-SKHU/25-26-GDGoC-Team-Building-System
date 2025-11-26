package com.skhu.gdgocteambuildingproject.teambuilding.service;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.ScheduleType;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.request.EnrollmentRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.ApplicantEnrollmentResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.EnrollmentAvailabilityResponseDto;
import java.util.List;

public interface EnrollmentService {
    void enroll(
            long userId,
            long ideaId,
            EnrollmentRequestDto requestDto
    );

    EnrollmentAvailabilityResponseDto getAvailabilityInfo(
            long ideaId,
            long applicantId
    );

    List<ApplicantEnrollmentResponseDto> getApplyHistory(long userId, ScheduleType scheduleType);
}
