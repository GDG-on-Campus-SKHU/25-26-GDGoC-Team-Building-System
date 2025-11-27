package com.skhu.gdgocteambuildingproject.teambuilding.model;

import com.skhu.gdgocteambuildingproject.Idea.domain.Idea;
import com.skhu.gdgocteambuildingproject.Idea.domain.IdeaEnrollment;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectSchedule;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.SentEnrollmentResponseDto;
import org.springframework.stereotype.Component;

@Component
public class SentEnrollmentMapper {

    public SentEnrollmentResponseDto map(
            IdeaEnrollment enrollment,
            ProjectSchedule schedule
    ) {
        Idea idea = enrollment.getIdea();
        Part enrollmentPart = enrollment.getPart();
        int applicantCount = getApplicantCount(idea, schedule, enrollmentPart);

        return SentEnrollmentResponseDto.builder()
                .enrollmentId(enrollment.getId())
                .choice(enrollment.getChoice())
                .enrollmentStatus(enrollment.getStatus())
                .ideaTitle(idea.getTitle())
                .ideaIntroduction(idea.getIntroduction())
                .enrollmentPart(enrollmentPart)
                .maxMemberCountOfPart(idea.getMaxMemberCountOf(enrollmentPart))
                .applicantCount(applicantCount)
                .build();
    }

    private int getApplicantCount(Idea idea, ProjectSchedule schedule, Part part) {
        return (int) idea.getEnrollmentsOf(schedule).stream()
                .filter(enrollment -> enrollment.getPart() == part)
                .count();
    }
}
