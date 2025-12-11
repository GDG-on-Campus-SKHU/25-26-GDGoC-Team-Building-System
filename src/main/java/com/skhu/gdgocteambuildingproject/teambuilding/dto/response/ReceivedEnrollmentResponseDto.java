package com.skhu.gdgocteambuildingproject.teambuilding.dto.response;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.EnrollmentStatus;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.Choice;
import lombok.Builder;

@Builder
public record ReceivedEnrollmentResponseDto(
        long enrollmentId,
        Choice choice,
        EnrollmentStatus enrollmentStatus,
        boolean enrollmentAcceptable,

        long applicantId,
        String applicantName,
        Part applicantPart,
        String applicantSchool
) {
}
