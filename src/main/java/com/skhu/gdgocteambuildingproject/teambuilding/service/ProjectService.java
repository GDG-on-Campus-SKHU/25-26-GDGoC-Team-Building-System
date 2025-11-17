package com.skhu.gdgocteambuildingproject.teambuilding.service;

import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.TeamBuildingInfoResponseDto;

public interface ProjectService {
    TeamBuildingInfoResponseDto findCurrentProjectInfo(long userId);
}
