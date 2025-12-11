package com.skhu.gdgocteambuildingproject.teambuilding.service;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.ScheduleType;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.request.EnrollmentDetermineRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.request.EnrollmentRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.EnrollmentAvailabilityResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.ReceivedEnrollmentResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.SentEnrollmentResponseDto;
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
