package com.skhu.gdgocteambuildingproject.teambuilding.service;

import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.PROJECT_NOT_EXIST;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.TeamBuildingInfoResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.exception.ProjectNotExistException;
import com.skhu.gdgocteambuildingproject.teambuilding.model.ProjectFilter;
import com.skhu.gdgocteambuildingproject.teambuilding.model.TeamBuildingInfoMapper;
import com.skhu.gdgocteambuildingproject.teambuilding.repository.TeamBuildingProjectRepository;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamBuildingServiceImpl implements TeamBuildingService {

    private final TeamBuildingProjectRepository projectRepository;

    private final ProjectFilter projectFilter;
    private final TeamBuildingInfoMapper teamBuildingInfoMapper;

    @Override
    public TeamBuildingInfoResponseDto findCurrentProjectInfo() {
        List<TeamBuildingProject> allProjects = projectRepository.findAll();

        // 한 시점에 동시에 여러 프로젝트가 진행될 수 없다고 가정
        List<TeamBuildingProject> unfinishedProjects = projectFilter.filterUnfinishedProjects(allProjects);
        TeamBuildingProject nearestProject = projectFilter.findEarliestProject(unfinishedProjects)
                .orElseThrow(() -> new ProjectNotExistException(PROJECT_NOT_EXIST.getMessage()));

        return teamBuildingInfoMapper.map(nearestProject);
    }
}
