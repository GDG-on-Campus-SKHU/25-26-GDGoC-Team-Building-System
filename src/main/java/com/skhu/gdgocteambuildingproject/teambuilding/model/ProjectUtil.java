package com.skhu.gdgocteambuildingproject.teambuilding.model;

import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.PROJECT_NOT_EXIST;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectSchedule;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.ScheduleType;
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

    private final TeamBuildingProjectRepository projectRepository;

    public Optional<TeamBuildingProject> findCurrentProject() {
        List<TeamBuildingProject> unfinishedProjects = findUnfinishedProjects();

        return findEarliestScheduledProject(unfinishedProjects)
                .or(() -> findUnscheduledProject(unfinishedProjects));
    }

    public Optional<TeamBuildingProject> findModifiableProject() {
        List<TeamBuildingProject> unstartedProjects = findUnstartedProjects();

        return findEarliestScheduledProject(unstartedProjects)
                .or(() -> findUnscheduledProject(unstartedProjects));
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

    private List<TeamBuildingProject> findUnstartedProjects() {
        // 아직 첫 일정이 시작하지 않은 프로젝트들만 조회
        return projectRepository.findProjectsWithScheduleNotStarted(
                ScheduleType.IDEA_REGISTRATION,
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
                .filter(p -> p.getStartDate() != null)
                .min(Comparator.comparing(TeamBuildingProject::getStartDate));
    }
}
