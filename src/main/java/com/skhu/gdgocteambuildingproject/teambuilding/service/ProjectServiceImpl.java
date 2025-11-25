package com.skhu.gdgocteambuildingproject.teambuilding.service;

import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.PROJECT_NOT_EXIST;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.USER_NOT_EXIST;

import com.skhu.gdgocteambuildingproject.admin.dto.project.ScheduleUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.ScheduleType;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.PastProjectResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.project.ProjectCreateRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.TeamBuildingInfoResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.model.PastProjectMapper;
import com.skhu.gdgocteambuildingproject.teambuilding.model.ProjectUtil;
import com.skhu.gdgocteambuildingproject.teambuilding.model.TeamBuildingInfoMapper;
import com.skhu.gdgocteambuildingproject.teambuilding.repository.TeamBuildingProjectRepository;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
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

    private final ProjectUtil projectUtil;
    private final TeamBuildingInfoMapper teamBuildingInfoMapper;
    private final PastProjectMapper pastProjectMapper;

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

        TeamBuildingProject currentProject = projectUtil.findCurrentProject(allProjects)
                .orElseThrow(() -> new IllegalStateException(PROJECT_NOT_EXIST.getMessage()));

        return teamBuildingInfoMapper.map(currentProject, user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PastProjectResponseDto> findPastProjects() {
        LocalDateTime now = LocalDateTime.now();

        List<TeamBuildingProject> pastProjects = findProjectsEndedBeforeThan(now);

        return pastProjects.stream()
                .map(pastProjectMapper::map)
                .toList();
    }

    @Override
    @Transactional
    public void updateSchedule(long projectId, ScheduleUpdateRequestDto requestDto) {
        TeamBuildingProject project = findProjectBy(projectId);

        project.updateSchedule(
                requestDto.scheduleType(),
                requestDto.startAt(),
                requestDto.endAt()
        );
    }

    private User findUserBy(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_EXIST.getMessage()));
    }

    private TeamBuildingProject findProjectBy(long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException(PROJECT_NOT_EXIST.getMessage()));
    }

    private List<TeamBuildingProject> findProjectsEndedBeforeThan(LocalDateTime criteriaTime) {
        return projectRepository.findProjectsWithScheduleEndedBefore(
                ScheduleType.FINAL_RESULT_ANNOUNCEMENT,
                criteriaTime
        );
    }
}
