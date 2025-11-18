package com.skhu.gdgocteambuildingproject.teambuilding.service;

import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.PROJECT_NOT_EXIST;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.USER_NOT_EXIST;

import com.skhu.gdgocteambuildingproject.admin.dto.project.ProjectCreateRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.TeamBuildingInfoResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.model.ProjectFilter;
import com.skhu.gdgocteambuildingproject.teambuilding.model.TeamBuildingInfoMapper;
import com.skhu.gdgocteambuildingproject.teambuilding.repository.TeamBuildingProjectRepository;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectServiceImpl implements ProjectService {

    private final TeamBuildingProjectRepository projectRepository;
    private final UserRepository userRepository;

    private final ProjectFilter projectFilter;
    private final TeamBuildingInfoMapper teamBuildingInfoMapper;

    @Override
    @Transactional
    public void createNewProject(ProjectCreateRequestDto requestDto) {
        TeamBuildingProject project = TeamBuildingProject.builder()
                .name(requestDto.projectName())
                .maxMemberCount(requestDto.maxMemberCount())
                .build();

        project.initSchedules();

        projectRepository.save(project);
    }

    @Override
    @Transactional(readOnly = true)
    public TeamBuildingInfoResponseDto findCurrentProjectInfo(long userId) {
        User user = findUserBy(userId);
        List<TeamBuildingProject> allProjects = projectRepository.findAll();

        List<TeamBuildingProject> unfinishedProjects = projectFilter.filterUnfinishedProjects(allProjects);

        TeamBuildingProject nearestProject = projectFilter.findEarliestScheduledProject(unfinishedProjects)
                .orElseGet(() -> findUnscheduledProject(unfinishedProjects));

        return teamBuildingInfoMapper.map(nearestProject, user);
    }

    private User findUserBy(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_EXIST.getMessage()));
    }

    private TeamBuildingProject findUnscheduledProject(List<TeamBuildingProject> projects) {
        return projectFilter.findUnscheduledProject(projects)
                .orElseThrow(() -> new EntityNotFoundException(PROJECT_NOT_EXIST.getMessage()));
    }
}
