package com.skhu.gdgocteambuildingproject.teambuilding.domain;

import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.SCHEDULE_ALREADY_INITIALIZED;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.SCHEDULE_NOT_EXIST;

import com.skhu.gdgocteambuildingproject.Idea.domain.Idea;
import com.skhu.gdgocteambuildingproject.global.entity.BaseEntity;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.ScheduleType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamBuildingProject extends BaseEntity {

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer maxMemberCount;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private final List<Idea> ideas = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private final List<ProjectSchedule> schedules = new ArrayList<>();

    public boolean isScheduled() {
        return schedules.stream()
                .allMatch(ProjectSchedule::isScheduled);
    }

    public boolean isUnscheduled() {
        return schedules.stream()
                .anyMatch(ProjectSchedule::isUnscheduled);
    }

    public LocalDateTime getEndDate() {
        return schedules.stream()
                .filter(schedule -> schedule.getType() == ScheduleType.FINAL_RESULT_ANNOUNCEMENT)
                .map(ProjectSchedule::getEndDate)
                .findAny()
                .orElse(null);
    }

    public LocalDateTime getStartDate() {
        return schedules.stream()
                .filter(schedule -> schedule.getType() == ScheduleType.FIRST_TEAM_BUILDING)
                .map(ProjectSchedule::getStartDate)
                .findAny()
                .orElse(null);
    }

    public void initSchedules() {
        if (!schedules.isEmpty()) {
            throw new IllegalStateException(SCHEDULE_ALREADY_INITIALIZED.getMessage());
        }

        for (ScheduleType type : ScheduleType.values()) {
            ProjectSchedule schedule = ProjectSchedule.builder()
                    .type(type)
                    .project(this)
                    .build();

            schedules.add(schedule);
        }
    }

    public Optional<ProjectSchedule> getCurrentSchedule() {
        LocalDateTime now = LocalDateTime.now();

        return schedules.stream()
                .filter(schedule -> now.isAfter(schedule.getStartDate()))
                .filter(schedule -> now.isBefore(schedule.getEndDate()))
                .findFirst();
    }

    public void updateSchedule(
            ScheduleType scheduleType,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        ProjectSchedule projectSchedule = schedules.stream()
                .filter(schedule -> schedule.getType() == scheduleType)
                .findAny()
                .orElseThrow(() -> new IllegalStateException(SCHEDULE_NOT_EXIST.getMessage()));

        projectSchedule.updateDates(startDate, endDate);
    }
}
