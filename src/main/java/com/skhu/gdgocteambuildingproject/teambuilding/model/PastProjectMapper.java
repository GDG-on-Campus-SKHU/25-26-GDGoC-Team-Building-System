package com.skhu.gdgocteambuildingproject.teambuilding.model;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.PastProjectResponseDto;
import org.springframework.stereotype.Component;

@Component
public class PastProjectMapper {
    public PastProjectResponseDto map(TeamBuildingProject project) {
        return PastProjectResponseDto.builder()
                .name(project.getName())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .build();
    }
}
