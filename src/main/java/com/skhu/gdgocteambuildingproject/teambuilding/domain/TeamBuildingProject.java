package com.skhu.gdgocteambuildingproject.teambuilding.domain;

import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.SCHEDULE_ALREADY_INITIALIZED;

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
        return getStartDate() != null && getEndDate() != null;
    }

    public boolean isUnscheduled() {
        return getStartDate() == null || getEndDate() == null;
    }

    public LocalDateTime getEndDate() {
        return schedules.stream()
                .filter(ProjectSchedule::scheduledEndDate)
                .map(ProjectSchedule::getEndDate)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

    public LocalDateTime getStartDate() {
        return schedules.stream()
                .filter(ProjectSchedule::scheduledStartDate)
                .map(ProjectSchedule::getStartDate)
                .min(LocalDateTime::compareTo)
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
}
