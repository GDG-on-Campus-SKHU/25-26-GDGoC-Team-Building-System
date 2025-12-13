package com.skhu.gdgocteambuildingproject.teambuilding.model.mapper;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.Idea;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.IdeaEnrollment;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectSchedule;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment.SentEnrollmentResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment.SentEnrollmentsResponseDto;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SentEnrollmentMapper {

    public SentEnrollmentsResponseDto map(
            List<IdeaEnrollment> enrollments,
            ProjectSchedule schedule
    ) {
        List<SentEnrollmentResponseDto> enrollmentDtos = enrollments.stream()
                .map(enrollment -> map(enrollment, schedule))
                .toList();

        boolean scheduleEnded = isScheduleEnded(schedule);

        return SentEnrollmentsResponseDto.builder()
                .scheduleEnded(scheduleEnded)
                .enrollments(enrollmentDtos)
                .build();
    }

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
                .enrollmentStatus(enrollment.getStatus().forApplicant())
                .ideaTitle(idea.getTitle())
                .ideaIntroduction(idea.getIntroduction())
                .enrollmentPart(enrollmentPart)
                .maxMemberCountOfPart(idea.getMaxMemberCountOf(enrollmentPart))
                .applicantCount(applicantCount)
                .build();
    }

    private boolean isScheduleEnded(ProjectSchedule schedule) {
        if (!schedule.isScheduled()) {
            return false;
        }

        return schedule.getEndDate().isBefore(LocalDateTime.now());
    }

    private int getApplicantCount(Idea idea, ProjectSchedule schedule, Part part) {
        return (int) idea.getEnrollmentsOf(schedule).stream()
                .filter(enrollment -> enrollment.getPart() == part)
                .count();
    }
}
