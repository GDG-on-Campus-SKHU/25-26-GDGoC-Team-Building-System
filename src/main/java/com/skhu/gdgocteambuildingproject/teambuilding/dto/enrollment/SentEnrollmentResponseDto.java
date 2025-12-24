package com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.EnrollmentStatus;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.Choice;
import lombok.Builder;

@Builder
public record SentEnrollmentResponseDto(
        long enrollmentId,
        long ideaId,
        Choice choice,
        EnrollmentStatus enrollmentStatus,

        String ideaTitle,
        String ideaIntroduction,

        Part enrollmentPart,
        int maxMemberCountOfPart,
        int applicantCount
) {
}
