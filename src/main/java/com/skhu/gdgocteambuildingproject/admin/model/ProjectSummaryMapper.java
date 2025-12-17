package com.skhu.gdgocteambuildingproject.admin.model;

import com.skhu.gdgocteambuildingproject.admin.dto.dashboard.ProjectSummaryResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectSchedule;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.teambuilding.model.ProjectUtil;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectSummaryMapper {

    private final ProjectUtil projectUtil;

    public ProjectSummaryResponseDto toSummaryResponse(TeamBuildingProject project) {
        return project.getCurrentSchedule()
                .map(schedule -> map(project, schedule))
                .orElseGet(() -> mapWithUpcomingSchedule(project));
    }

    private ProjectSummaryResponseDto map(TeamBuildingProject project, ProjectSchedule schedule) {
        return ProjectSummaryResponseDto.builder()
                .id(project.getId())
                .projectName(project.getName())
                .ideaCount(project.getIdeas().size())
                .currentParticipants(project.getParticipants().size())
                .maxMemberCount(project.getMaxMemberCount())
                .currentScheduleType(schedule.getType())
                .currentScheduleDeadline(getDeadline(project, schedule))
                .build();
    }

    private ProjectSummaryResponseDto mapWithUpcomingSchedule(TeamBuildingProject project) {
        return projectUtil.findUpcomingSchedule(project)
                .map(upcomingSchedule -> map(project, upcomingSchedule))
                .orElse(null);
    }

    private LocalDateTime getDeadline(TeamBuildingProject project, ProjectSchedule schedule) {
        if (schedule.getEndDate() == null) {
            // endDate가 null일 경우, 다음 일정의 시작일을 현재 일정의 마감일로 취급
            return project.getNextScheduleOf(schedule.getType())
                    .map(ProjectSchedule::getStartDate)
                    .orElse(null);
        }

        return schedule.getEndDate();
    }
}
