package com.skhu.gdgocteambuildingproject.teambuilding.model;

import com.skhu.gdgocteambuildingproject.Idea.domain.IdeaEnrollment;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.ReceivedEnrollmentResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class ReceivedEnrollmentMapper {
    public ReceivedEnrollmentResponseDto map(IdeaEnrollment enrollment) {
        User applicant = enrollment.getApplicant();

        return ReceivedEnrollmentResponseDto.builder()
                .enrollmentId(enrollment.getId())
                .choice(enrollment.getChoice())
                .enrollmentStatus(enrollment.getStatus())
                .applicantId(applicant.getId())
                .applicantName(applicant.getName())
                .applicantPart(applicant.getPart())
                .applicantSchool(applicant.getSchool())
                .build();
    }
}
