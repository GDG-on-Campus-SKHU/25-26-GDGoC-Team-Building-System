package com.skhu.gdgocteambuildingproject.teambuilding.model.mapper;

import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.*;
import static com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.ScheduleType.*;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectSchedule;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.ScheduleType;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment.EnrollmentReadableResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class EnrollmentReadableMapper {

    public List<EnrollmentReadableResponseDto> mapReadableSchedules(
            List<ScheduleType> scheduleTypes,
            User user,
            TeamBuildingProject project,
            LocalDateTime now
    ) {
        return scheduleTypes.stream()
                .map(scheduleType -> mapReadableSchedule(scheduleType, user, project, now))
                .toList();
    }

    public EnrollmentReadableResponseDto mapReadableSchedule(
            ScheduleType scheduleType,
            User user,
            TeamBuildingProject project,
            LocalDateTime now
    ) {
        boolean readable = isReadable(user, project, scheduleType, now);

        return EnrollmentReadableResponseDto.builder()
                .scheduleType(scheduleType)
                .readable(readable)
                .build();
    }

    private boolean isReadable(
            User user,
            TeamBuildingProject project,
            ScheduleType scheduleType,
            LocalDateTime now
    ) {
        return switch (scheduleType) {
            case FIRST_TEAM_BUILDING -> isReadableFirstTeamBuilding(project, now);
            case SECOND_TEAM_BUILDING -> isReadableSecondTeamBuilding(user, project, now);
            default -> throw new IllegalArgumentException(NOT_ENROLLMENT_SCHEDULE.getMessage());
        };
    }

    /**
     * 1차 팀빌딩의 지원 내역이 조회 가능한지를 반환한다.
     * 1차 팀빌딩은 해당 일정이 시작했다면 조회할 수 있다.
     */
    private boolean isReadableFirstTeamBuilding(
            TeamBuildingProject project,
            LocalDateTime now
    ) {
        ProjectSchedule schedule = project.getScheduleFrom(FIRST_TEAM_BUILDING);

        return isScheduleStarted(schedule, now);
    }

    /**
     * 2차 팀빌딩의 지원 내역이 조회 가능한지를 반환한다.
     * 2차 팀빌딩이 시작했으며, 1차 팀빌딩 때 멤버로 수락되지 않았다면 조회할 수 있다.
     */
    private boolean isReadableSecondTeamBuilding(
            User user,
            TeamBuildingProject project,
            LocalDateTime now
    ) {
        ProjectSchedule schedule = project.getScheduleFrom(SECOND_TEAM_BUILDING);

        return isScheduleStarted(schedule, now)
                && !isAcceptedAtFirstTeamBuilding(user, project);
    }

    private boolean isAcceptedAtFirstTeamBuilding(User user, TeamBuildingProject project) {
        return user.getMembers().stream()
                .filter(member -> project.equals(member.getIdea().getProject()))
                .anyMatch(member -> member.getAcceptedAt() == FIRST_TEAM_BUILDING);
    }

    private boolean isScheduleStarted(ProjectSchedule schedule, LocalDateTime now) {
        if (schedule.getStartDate() == null) {
            return false;
        }
        return !now.isBefore(schedule.getStartDate());
    }
}

