package com.skhu.gdgocteambuildingproject.teambuilding.service;

import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.PROJECT_ALREADY_EXISTS;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.PROJECT_NOT_EXIST;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.USER_NOT_EXIST;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.Idea;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.project.ProjectInfoPageResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.project.ProjectInfoResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.project.ModifiableProjectResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.project.ProjectNameUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.project.ProjectUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.project.ScheduleUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.project.SchoolResponseDto;
import com.skhu.gdgocteambuildingproject.global.pagination.PageInfo;
import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.ScheduleType;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.project.PastProjectResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.project.ProjectCreateRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.project.ProjectParticipationAvailabilityResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.project.TeamBuildingInfoResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.model.ParticipationUtil;
import com.skhu.gdgocteambuildingproject.teambuilding.model.ProjectUtil;
import com.skhu.gdgocteambuildingproject.teambuilding.model.mapper.ModifiableProjectMapper;
import com.skhu.gdgocteambuildingproject.teambuilding.model.mapper.PastProjectMapper;
import com.skhu.gdgocteambuildingproject.teambuilding.model.mapper.ProjectInfoMapper;
import com.skhu.gdgocteambuildingproject.teambuilding.model.mapper.TeamBuildingInfoMapper;
import com.skhu.gdgocteambuildingproject.teambuilding.repository.IdeaRepository;
import com.skhu.gdgocteambuildingproject.teambuilding.repository.TeamBuildingProjectRepository;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectServiceImpl implements ProjectService {

    private final TeamBuildingProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final IdeaRepository ideaRepository;
    private final EntityManager entityManager;

    private final ProjectUtil projectUtil;
    private final ParticipationUtil participationUtil;
    private final TeamBuildingInfoMapper teamBuildingInfoMapper;
    private final PastProjectMapper pastProjectMapper;
    private final ProjectInfoMapper projectInfoMapper;
    private final ModifiableProjectMapper modifiableProjectMapper;

    @Override
    @Transactional
    public void createNewProject(ProjectCreateRequestDto requestDto) {
        validateNewProjectCreatable();

        TeamBuildingProject project = TeamBuildingProject.builder()
                .name(requestDto.projectName())
                .maxMemberCount(requestDto.maxMemberCount())
                .build();

        project.initSchedules();

        projectRepository.save(project);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectInfoPageResponseDto findProjects(int page, int size, String sortBy, SortOrder order) {
        Pageable pagination = setupPagination(page, size, sortBy, order);
        Page<TeamBuildingProject> projects = projectRepository.findAll(pagination);

        List<ProjectInfoResponseDto> projectDtos = projects.stream()
                .map(projectInfoMapper::map)
                .toList();

        return ProjectInfoPageResponseDto.builder()
                .projects(projectDtos)
                .pageInfo(PageInfo.from(projects))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public TeamBuildingInfoResponseDto findCurrentProjectInfo(long userId) {
        User user = findUserBy(userId);

        TeamBuildingProject currentProject = projectUtil.findCurrentProject()
                .orElseThrow(() -> new EntityNotFoundException(PROJECT_NOT_EXIST.getMessage()));

        participationUtil.validateParticipation(userId, currentProject.getId());

        validateProjectScheduled(currentProject);

        return teamBuildingInfoMapper.map(currentProject, user);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectParticipationAvailabilityResponseDto checkParticipationAvailability(long userId) {
        Optional<TeamBuildingProject> currentProject = projectUtil.findCurrentProject();

        if (currentProject.isEmpty()) {
            return ProjectParticipationAvailabilityResponseDto.builder()
                    .available(false)
                    .build();
        }

        boolean participated = participationUtil.isParticipated(userId, currentProject.get().getId());

        return ProjectParticipationAvailabilityResponseDto.builder()
                .available(participated)
                .build();
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
    @Transactional(readOnly = true)
    public ModifiableProjectResponseDto findModifiableProject() {
        TeamBuildingProject project = projectUtil.findModifiableProject()
                .orElseThrow(() -> new EntityNotFoundException(PROJECT_NOT_EXIST.getMessage()));

        return modifiableProjectMapper.map(project);
    }

    @Override
    @Transactional
    public void updateProject(long projectId, ProjectUpdateRequestDto requestDto) {
        TeamBuildingProject project = findProjectBy(projectId);

        List<User> participants = findParticipatingUsers(requestDto.participantUserIds());

        project.update(
                requestDto.maxMemberCount(),
                requestDto.availableParts(),
                requestDto.topics(),
                participants
        );

        for (ScheduleUpdateRequestDto schedule : requestDto.schedules()) {
            project.updateSchedule(
                    schedule.scheduleType(),
                    schedule.startAt(),
                    schedule.endAt()
            );
        }
    }

    @Override
    @Transactional
    public void updateProjectName(long projectId, ProjectNameUpdateRequestDto requestDto) {
        TeamBuildingProject project = findProjectBy(projectId);

        project.updateName(requestDto.projectName());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SchoolResponseDto> findSchools() {
        List<String> schools = userRepository.findDistinctSchools();

        return schools.stream()
                .map(SchoolResponseDto::new)
                .toList();
    }

    @Override
    @Transactional
    public void deleteProject(long projectId) {
        TeamBuildingProject project = findProjectBy(projectId);

        removeIdeasOf(project);

        projectRepository.delete(project);
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

    private Pageable setupPagination(
            int page,
            int size,
            String sortBy,
            SortOrder order
    ) {
        return PageRequest.of(
                page,
                size,
                order.sort(sortBy)
        );
    }

    private List<User> findParticipatingUsers(List<Long> participantUserIds) {
        return participantUserIds.stream()
                .map(this::findUserBy)
                .toList();
    }

    private void validateProjectScheduled(TeamBuildingProject currentProject) {
        if (currentProject.isUnscheduled()) {
            throw new EntityNotFoundException(PROJECT_NOT_EXIST.getMessage());
        }
    }

    /**
     * 새 프로젝트를 등록할 수 있는지 검증하는 메서드.
     * 지금 진행중이거나 예정된 프로젝트가 있으면 생성할 수 없음.
     */
    private void validateNewProjectCreatable() {
        if (projectUtil.findCurrentProject().isPresent()) {
            throw new IllegalStateException(PROJECT_ALREADY_EXISTS.getMessage());
        }
    }

    /**
     * 프로젝트에 등록된 모든 아이디어를 삭제한다.
     * 프로젝트를 삭제할 때 발생하는 외래키 제약조건 문제를 회피하기 위해 사용.
     */
    private void removeIdeasOf(TeamBuildingProject project) {
        List<Idea> ideas = ideaRepository.findAllByProjectIdIncludeDeleted(project.getId());
        ideaRepository.deleteAll(ideas);
        entityManager.flush();
    }
}
