package com.skhu.gdgocteambuildingproject.teambuilding.model;

import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.NOT_PARTICIPATED;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.PROJECT_NOT_EXIST;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectSchedule;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.ScheduleType;
import com.skhu.gdgocteambuildingproject.teambuilding.repository.ProjectParticipantRepository;
import com.skhu.gdgocteambuildingproject.teambuilding.repository.TeamBuildingProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectUtil {

    private final ProjectParticipantRepository participantRepository;
    private final TeamBuildingProjectRepository projectRepository;

    public void validateParticipation(long userId, long projectId) {
        boolean participated = participantRepository.existsByUserIdAndProjectId(userId, projectId);

        if (!participated) {
            throw new IllegalStateException(NOT_PARTICIPATED.getMessage());
        }
    }

    public Optional<TeamBuildingProject> findCurrentProject() {
        List<TeamBuildingProject> unfinishedProjects = findUnfinishedProjects();

        return findEarliestScheduledProject(unfinishedProjects)
                .or(() -> findUnscheduledProject(unfinishedProjects));
    }

    public Optional<ProjectSchedule> findCurrentSchedule() {
        TeamBuildingProject currentProject = findCurrentProject()
                .orElseThrow(() -> new EntityNotFoundException(PROJECT_NOT_EXIST.getMessage()));

        return currentProject.getCurrentSchedule();
    }

    private List<TeamBuildingProject> findUnfinishedProjects() {
        // 아직 마지막 일정이 끝나지 않은 프로젝트들만 조회
        return projectRepository.findProjectsWithScheduleNotEndedBefore(
                ScheduleType.FINAL_RESULT_ANNOUNCEMENT,
                LocalDateTime.now()
        );
    }

    private Optional<TeamBuildingProject> findUnscheduledProject(List<TeamBuildingProject> projects) {
        return projects.stream()
                .filter(TeamBuildingProject::isUnscheduled)
                .findFirst();
    }

    private Optional<TeamBuildingProject> findEarliestScheduledProject(List<TeamBuildingProject> projects) {
        return projects.stream()
                .filter(TeamBuildingProject::isScheduled)
                .min(Comparator.comparing(TeamBuildingProject::getStartDate));
    }
}
