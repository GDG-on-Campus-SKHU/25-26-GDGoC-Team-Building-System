package com.skhu.gdgocteambuildingproject.teambuilding.model.mapper;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.Idea;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.IdeaEnrollment;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectSchedule;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment.EnrollmentAvailabilityResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment.EnrollmentAvailabilitySimpleResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EnrollmentAvailabilityMapper {
    private final PartAvailabilityMapper partMapper;
    private final ChoiceAvailabilityMapper choiceMapper;

    public EnrollmentAvailabilityResponseDto map(
            TeamBuildingProject project,
            ProjectSchedule schedule,
            Idea idea,
            User applicant
    ) {
        User creator = idea.getCreator();

        return EnrollmentAvailabilityResponseDto.builder()
                .projectName(project.getName())
                .scheduleType(schedule.getType())
                .ideaTitle(idea.getTitle())
                .ideaIntroduction(idea.getIntroduction())
                .creatorSchool(creator.getSchool())
                .creatorPart(idea.getCreatorPart())
                .creatorName(creator.getName())
                .choiceAvailabilities(choiceMapper.map(applicant, schedule))
                .partAvailabilities(partMapper.map(idea))
                .build();
    }

    public EnrollmentAvailabilitySimpleResponseDto mapSimple(
            TeamBuildingProject project,
            Idea idea,
            User user
    ) {
        return EnrollmentAvailabilitySimpleResponseDto.builder()
                .canEnroll(canEnroll(project, idea, user))
                .isCreator(idea.isCreator(user))
                .build();
    }

    private boolean canEnroll(
            TeamBuildingProject project,
            Idea idea,
            User applicant
    ) {
        if (!isIdeaInProject(idea, project)) {
            return false;
        }

        if (!isEnrollmentSchedule(project)) {
            return false;
        }

        if (applicant.isMemberOf(project)) {
            return false;
        }

        return !isAlreadyEnrolled(project, idea, applicant);
    }

    private boolean isIdeaInProject(Idea idea, TeamBuildingProject project) {
        return idea.getProject().equals(project);
    }

    private boolean isEnrollmentSchedule(TeamBuildingProject project) {
        return project.getCurrentSchedule()
                .map(ProjectSchedule::isEnrollmentAvailable)
                .orElse(false);
    }

    private boolean isAlreadyEnrolled(
            TeamBuildingProject project,
            Idea idea,
            User applicant
    ) {
        return project.getCurrentSchedule()
                .map(schedule -> hasEnrollmentOf(schedule, idea, applicant))
                .orElse(false);
    }

    private boolean hasEnrollmentOf(
            ProjectSchedule schedule,
            Idea idea,
            User applicant
    ) {
        List<IdeaEnrollment> enrollments = applicant.getEnrollments();

        return enrollments.stream()
                .filter(enrollment -> enrollment.getSchedule().equals(schedule))
                .anyMatch(enrollment -> enrollment.getIdea().equals(idea));
    }
}
