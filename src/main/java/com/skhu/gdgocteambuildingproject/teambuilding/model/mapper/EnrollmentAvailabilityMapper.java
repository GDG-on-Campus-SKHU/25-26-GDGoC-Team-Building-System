package com.skhu.gdgocteambuildingproject.teambuilding.model.mapper;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.Idea;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectSchedule;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment.EnrollmentAvailabilityResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.User;
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
}
