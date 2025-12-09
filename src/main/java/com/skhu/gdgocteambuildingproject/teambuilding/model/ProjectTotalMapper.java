package com.skhu.gdgocteambuildingproject.teambuilding.model;

import com.skhu.gdgocteambuildingproject.admin.dto.project.ProjectTotalResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectTotalMapper {
    private final ProjectAvailablePartMapper availablePartMapper;
    private final ProjectScheduleMapper scheduleMapper;

    public ProjectTotalResponseDto map(TeamBuildingProject project) {
        return ProjectTotalResponseDto.builder()
                .projectId(project.getId())
                .projectName(project.getName())
                .maxMemberCount(project.getMaxMemberCount())
                .availableParts(availablePartMapper.map(project))
                .schedules(scheduleMapper.map(project.getSchedules()))
                .build();
    }
}

