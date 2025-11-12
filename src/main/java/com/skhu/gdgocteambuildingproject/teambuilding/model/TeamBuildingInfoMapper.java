package com.skhu.gdgocteambuildingproject.teambuilding.model;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.TeamBuildingInfoResponseDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamBuildingInfoMapper {
    private final ProjectScheduleMapper scheduleMapper;

    public TeamBuildingInfoResponseDto map(TeamBuildingProject project) {
        return TeamBuildingInfoResponseDto.builder()
                .projectId(project.getId())
                .projectName(project.getName())
                .maxMemberCount(project.getMaxMemberCount())
                .schedules(scheduleMapper.map(project.getSchedules()))
                .build();
    }
}
