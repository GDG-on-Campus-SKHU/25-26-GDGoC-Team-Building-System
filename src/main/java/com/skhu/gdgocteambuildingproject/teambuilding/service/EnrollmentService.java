package com.skhu.gdgocteambuildingproject.teambuilding.service;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.ScheduleType;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment.EnrollmentAvailabilityResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment.EnrollmentDetermineRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment.EnrollmentRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment.EnrollmentReadableResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment.IdeaEnrollmentAvailabilityResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment.ReceivedEnrollmentsResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment.SentEnrollmentsResponseDto;
import java.util.List;

public interface EnrollmentService {

    void enroll(
            long userId,
            long ideaId,
            EnrollmentRequestDto requestDto
    );

    void determineEnrollment(
            long userId,
            long enrollmentId,
            EnrollmentDetermineRequestDto requestDto
    );

    IdeaEnrollmentAvailabilityResponseDto canEnroll(long ideaId, long userId);

    EnrollmentAvailabilityResponseDto getAvailabilityInfo(
            long ideaId,
            long applicantId
    );

    SentEnrollmentsResponseDto getSentEnrollments(
            long userId,
            ScheduleType scheduleType
    );

    ReceivedEnrollmentsResponseDto getReceivedEnrollments(
            long userId,
            ScheduleType scheduleType
    );

    List<EnrollmentReadableResponseDto> getSentEnrollmentReadabilities(long userId);

    void cancelEnrollment(
            long userId,
            long enrollmentId
    );
}
