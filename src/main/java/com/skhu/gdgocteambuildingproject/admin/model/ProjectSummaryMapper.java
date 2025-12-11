package com.skhu.gdgocteambuildingproject.admin.model;

import com.skhu.gdgocteambuildingproject.admin.dto.dashboard.ProjectSummaryResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectSchedule;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.ScheduleType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProjectSummaryMapper {

    public ProjectSummaryResponseDto toSummaryResponse(TeamBuildingProject project) {
        ProjectSchedule currentSchedule = project.getCurrentSchedule().get();

        ScheduleType type = currentSchedule.getType();
        LocalDateTime deadline = currentSchedule.getEndDate();

        return ProjectSummaryResponseDto.builder()
                .id(project.getId())
                .projectName(project.getName())
                .ideaCount(project.getIdeas().size())
                .currentParticipants(project.getParticipants().size())
                .maxMemberCount(project.getMaxMemberCount())
                .currentScheduleType(type.name())
                .currentScheduleDeadline(deadline)
                .build();
    }
}
