package com.skhu.gdgocteambuildingproject.teambuilding.model.mapper;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.Idea;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.IdeaEnrollment;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.EnrollmentStatus;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectSchedule;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment.SentEnrollmentResponseDto;
import java.time.LocalDateTime;
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

        LocalDateTime now = LocalDateTime.now();
        boolean scheduleEnded = isScheduleEnded(schedule, now);

        return SentEnrollmentResponseDto.builder()
                .enrollmentId(enrollment.getId())
                .choice(enrollment.getChoice())
                .enrollmentStatus(convertToProperStatus(enrollment.getStatus()))
                .ideaTitle(idea.getTitle())
                .ideaIntroduction(idea.getIntroduction())
                .enrollmentPart(enrollmentPart)
                .maxMemberCountOfPart(idea.getMaxMemberCountOf(enrollmentPart))
                .applicantCount(applicantCount)
                .scheduleEnded(scheduleEnded)
                .build();
    }

    private boolean isScheduleEnded(ProjectSchedule schedule, LocalDateTime now) {
        if (!schedule.isScheduled()) {
            return false;
        }

        return schedule.getEndDate().isBefore(now);
    }

    private int getApplicantCount(Idea idea, ProjectSchedule schedule, Part part) {
        return (int) idea.getEnrollmentsOf(schedule).stream()
                .filter(enrollment -> enrollment.getPart() == part)
                .count();
    }

    private EnrollmentStatus convertToProperStatus(EnrollmentStatus status) {
        if (status.isWaitingToConfirm()) {
            return EnrollmentStatus.WAITING;
        }

        return status;
    }
}
