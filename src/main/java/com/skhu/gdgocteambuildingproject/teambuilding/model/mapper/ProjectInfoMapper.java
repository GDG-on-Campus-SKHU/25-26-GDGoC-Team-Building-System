package com.skhu.gdgocteambuildingproject.teambuilding.model.mapper;

import com.skhu.gdgocteambuildingproject.teambuilding.dto.project.ProjectInfoResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import org.springframework.stereotype.Component;

@Component
public class ProjectInfoMapper {

    public ProjectInfoResponseDto map(TeamBuildingProject project) {
        return ProjectInfoResponseDto.builder()
                .projectId(project.getId())
                .projectName(project.getName())
                .startAt(project.getStartDate())
                .endAt(project.getEndDate())
                .build();
    }
}
