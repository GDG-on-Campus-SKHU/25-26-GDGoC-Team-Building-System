package com.skhu.gdgocteambuildingproject.teambuilding.service;

import com.skhu.gdgocteambuildingproject.admin.dto.project.ProjectInfoPageResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.project.ModifiableProjectResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.project.ProjectUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.admin.dto.project.ScheduleUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.PastProjectResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.project.ProjectCreateRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.TeamBuildingInfoResponseDto;
import java.util.List;

public interface ProjectService {
    void createNewProject(ProjectCreateRequestDto requestDto);

    ProjectInfoPageResponseDto findProjects(
            int page,
            int size,
            String sortBy,
            SortOrder order
    );

    TeamBuildingInfoResponseDto findCurrentProjectInfo(long userId);

    List<PastProjectResponseDto> findPastProjects();

    ModifiableProjectResponseDto findModifiableProject();

    void updateProject(long projectId, ProjectUpdateRequestDto requestDto);

    void updateSchedule(long projectId, ScheduleUpdateRequestDto requestDto);
}
