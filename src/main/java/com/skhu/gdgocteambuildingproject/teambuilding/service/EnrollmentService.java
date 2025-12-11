package com.skhu.gdgocteambuildingproject.teambuilding.service;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.ScheduleType;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment.EnrollmentDetermineRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment.EnrollmentRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment.EnrollmentAvailabilityResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment.ReceivedEnrollmentResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment.SentEnrollmentResponseDto;
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

    EnrollmentAvailabilityResponseDto getAvailabilityInfo(
            long ideaId,
            long applicantId
    );

    List<SentEnrollmentResponseDto> getSentEnrollments(
            long userId,
            ScheduleType scheduleType
    );

    List<ReceivedEnrollmentResponseDto> getReceivedEnrollments(
            long userId,
            ScheduleType scheduleType
    );

    void cancelEnrollment(
            long userId,
            long enrollmentId
    );
}
