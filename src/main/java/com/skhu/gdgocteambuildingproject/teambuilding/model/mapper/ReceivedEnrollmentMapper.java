package com.skhu.gdgocteambuildingproject.teambuilding.model.mapper;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.Idea;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.IdeaEnrollment;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectSchedule;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment.ReceivedEnrollmentResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment.ReceivedEnrollmentsResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ReceivedEnrollmentMapper {
    public ReceivedEnrollmentsResponseDto map(
            List<IdeaEnrollment> enrollments,
            Idea idea,
            ProjectSchedule schedule
    ) {
        List<ReceivedEnrollmentResponseDto> enrollmentDtos = enrollments.stream()
                .map(enrollment -> map(enrollment, idea))
                .toList();

        return ReceivedEnrollmentsResponseDto.builder()
                .scheduleEnded(isScheduleEnded(schedule))
                .enrollments(enrollmentDtos)
                .build();
    }

    public ReceivedEnrollmentResponseDto map(IdeaEnrollment enrollment, Idea idea) {
        User applicant = enrollment.getApplicant();
        Part part = enrollment.getPart();

        return ReceivedEnrollmentResponseDto.builder()
                .enrollmentId(enrollment.getId())
                .choice(enrollment.getChoice())
                .enrollmentStatus(enrollment.getStatus().forCreator())
                .enrollmentAcceptable(idea.isEnrollmentAvailable(part))
                .applicantId(applicant.getId())
                .applicantName(applicant.getName())
                .applicantPart(part)
                .applicantSchool(applicant.getSchool())
                .build();
    }

    private boolean isScheduleEnded(ProjectSchedule schedule) {
        if (!schedule.isScheduled()) {
            return false;
        }

        return schedule.getEndDate().isBefore(LocalDateTime.now());
    }
}
