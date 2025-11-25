package com.skhu.gdgocteambuildingproject.teambuilding.dto.response;

import com.skhu.gdgocteambuildingproject.Idea.domain.enumtype.EnrollmentStatus;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.Choice;
import lombok.Builder;

@Builder
public record ApplicantEnrollmentResponseDto(
        long enrollmentId,
        Choice choice,
        EnrollmentStatus enrollmentStatus,

        String ideaTitle,
        String ideaIntroduction,

        Part enrollmentPart,
        int maxMemberCountOfPart,
        int applicantCount
) {
}
