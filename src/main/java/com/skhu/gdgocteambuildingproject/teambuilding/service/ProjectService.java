package com.skhu.gdgocteambuildingproject.teambuilding.service;

import com.skhu.gdgocteambuildingproject.admin.dto.project.ProjectCreateRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.TeamBuildingInfoResponseDto;

public interface ProjectService {
    void createNewProject(ProjectCreateRequestDto requestDto);

    TeamBuildingInfoResponseDto findCurrentProjectInfo();
}
