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
        List<TeamBuildingProject> unfinishedProjects = findUnfinishedProjects();

        return findEarliestScheduledProject(unfinishedProjects)
                .or(() -> findUnscheduledProject(unfinishedProjects));
    }

    public Optional<ProjectSchedule> findCurrentSchedule() {
        TeamBuildingProject currentProject = findCurrentProject()
                .orElseThrow(() -> new EntityNotFoundException(PROJECT_NOT_EXIST.getMessage()));

        return currentProject.getCurrentSchedule();
    }

    public Optional<ProjectSchedule> findUpcomingSchedule(TeamBuildingProject project) {
        LocalDateTime now = LocalDateTime.now();

        // 이후에 시작하는 일정 중 가장 빠른 일정을 반환
        return project.getSchedules().stream()
                .filter(ProjectSchedule::isScheduled)
                .filter(schedule -> schedule.getStartDate() != null)
                .filter(schedule -> schedule.getStartDate().isAfter(now))
                .min(Comparator.comparing(ProjectSchedule::getStartDate));
    }

    public List<TeamBuildingProject> findPastProjects() {
        // 최종 발표 일정의 시작일이 현재보다 과거인 프로젝트들만 조회
        return projectRepository.findProjectsWithScheduleStartedBefore(
                ScheduleType.FINAL_RESULT_ANNOUNCEMENT,
                LocalDateTime.now()
        );
    }

    private List<TeamBuildingProject> findUnfinishedProjects() {
        // 아직 최종 발표 종료일이 지나지 않은 프로젝트들만 조회
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
                .filter(p -> p.getStartDate() != null)
                .min(Comparator.comparing(TeamBuildingProject::getStartDate));
    }
}
