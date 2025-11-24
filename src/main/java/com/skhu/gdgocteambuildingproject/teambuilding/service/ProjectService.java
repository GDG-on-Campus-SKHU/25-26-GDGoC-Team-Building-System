package com.skhu.gdgocteambuildingproject.teambuilding.service;

import com.skhu.gdgocteambuildingproject.admin.dto.project.ScheduleUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.PastProjectResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.project.ProjectCreateRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.TeamBuildingInfoResponseDto;
import java.util.List;

public interface ProjectService {
    void createNewProject(ProjectCreateRequestDto requestDto);

    TeamBuildingInfoResponseDto findCurrentProjectInfo(long userId);

    List<PastProjectResponseDto> findPastProjects();

    void updateSchedule(long projectId, ScheduleUpdateRequestDto requestDto);
}
