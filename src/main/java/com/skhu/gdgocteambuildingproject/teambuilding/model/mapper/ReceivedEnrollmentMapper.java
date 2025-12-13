package com.skhu.gdgocteambuildingproject.teambuilding.model.mapper;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.Idea;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.IdeaEnrollment;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment.ReceivedEnrollmentResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class ReceivedEnrollmentMapper {
    public ReceivedEnrollmentResponseDto map(IdeaEnrollment enrollment, Idea idea) {
        User applicant = enrollment.getApplicant();
        Part part = enrollment.getPart();

        return ReceivedEnrollmentResponseDto.builder()
                .enrollmentId(enrollment.getId())
                .choice(enrollment.getChoice())
                .enrollmentStatus(enrollment.getStatus().confirmedStatus())
                .enrollmentAcceptable(idea.isEnrollmentAvailable(part))
                .applicantId(applicant.getId())
                .applicantName(applicant.getName())
                .applicantPart(part)
                .applicantSchool(applicant.getSchool())
                .build();
    }
}
