package com.skhu.gdgocteambuildingproject.teambuilding.model;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class ProjectFilter {
    public List<TeamBuildingProject> filterUnfinishedProjects(List<TeamBuildingProject> projects) {
        LocalDateTime now = LocalDateTime.now();

        return projects.stream()
                .filter(TeamBuildingProject::hasSchedules)
                .filter(project -> project.getEndDate().isAfter(now))
                .toList();
    }

    public Optional<TeamBuildingProject> findEarliestProject(List<TeamBuildingProject> projects) {
        return projects.stream()
                .min(Comparator.comparing(TeamBuildingProject::getStartDate));
    }
}
