package com.skhu.gdgocteambuildingproject.teambuilding.service;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.ScheduleType;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.ReceivedEnrollmentResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.SentEnrollmentResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.EnrollmentAvailabilityResponseDto;
import java.util.List;

public interface EnrollmentService {
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
}
