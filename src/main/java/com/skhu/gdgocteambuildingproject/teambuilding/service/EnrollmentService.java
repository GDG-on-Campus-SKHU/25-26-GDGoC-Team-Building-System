package com.skhu.gdgocteambuildingproject.teambuilding.service;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.ScheduleType;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.request.EnrollmentDetermineRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.ApplicantEnrollmentResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.EnrollmentAvailabilityResponseDto;
import java.util.List;

public interface EnrollmentService {

    void determineEnrollment(
            long userId,
            long enrollmentId,
            EnrollmentDetermineRequestDto requestDto
    );

    EnrollmentAvailabilityResponseDto getAvailabilityInfo(
            long ideaId,
            long applicantId
    );

    List<ApplicantEnrollmentResponseDto> getApplyHistory(long userId, ScheduleType scheduleType);
}
