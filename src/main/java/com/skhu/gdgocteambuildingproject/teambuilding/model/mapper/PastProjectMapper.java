package com.skhu.gdgocteambuildingproject.teambuilding.model.mapper;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.project.PastProjectResponseDto;
import org.springframework.stereotype.Component;

@Component
public class PastProjectMapper {
    public PastProjectResponseDto map(TeamBuildingProject project) {
        return PastProjectResponseDto.builder()
                .projectId(project.getId())
                .name(project.getName())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .build();
    }
}
