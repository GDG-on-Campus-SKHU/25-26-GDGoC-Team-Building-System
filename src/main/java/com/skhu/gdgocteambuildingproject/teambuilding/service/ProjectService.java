package com.skhu.gdgocteambuildingproject.teambuilding.service;

import com.skhu.gdgocteambuildingproject.admin.dto.project.ProjectInfoPageResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.project.ModifiableProjectResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.project.ProjectUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.admin.dto.project.SchoolResponseDto;
import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.project.PastProjectResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.project.ProjectCreateRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.project.ProjectParticipationAvailabilityResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.project.TeamBuildingInfoResponseDto;
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

    ProjectParticipationAvailabilityResponseDto checkParticipationAvailability(long userId);

    List<PastProjectResponseDto> findPastProjects();

    ModifiableProjectResponseDto findModifiableProject();

    void updateProject(long projectId, ProjectUpdateRequestDto requestDto);

    List<SchoolResponseDto> findSchools();

    void deleteProject(long projectId);
}
