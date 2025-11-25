package com.skhu.gdgocteambuildingproject.teambuilding.model;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class ProjectUtil {
    public Optional<TeamBuildingProject> findCurrentProject(List<TeamBuildingProject> projects) {
        List<TeamBuildingProject> unfinishedProjects = filterUnfinishedProjects(projects);

        return findEarliestScheduledProject(unfinishedProjects)
                .or(() -> findUnscheduledProject(unfinishedProjects));
    }

    private List<TeamBuildingProject> filterUnfinishedProjects(List<TeamBuildingProject> projects) {
        LocalDateTime now = LocalDateTime.now();

        return projects.stream()
                .filter(project -> project.isUnscheduled() || project.getEndDate().isAfter(now))
                .toList();
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
