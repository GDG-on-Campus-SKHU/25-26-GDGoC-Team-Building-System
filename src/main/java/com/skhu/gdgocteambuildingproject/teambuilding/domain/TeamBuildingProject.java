package com.skhu.gdgocteambuildingproject.teambuilding.domain;

import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.ALREADY_PARTICIPATED;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.SCHEDULE_ALREADY_INITIALIZED;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.SCHEDULE_NOT_EXIST;

import com.skhu.gdgocteambuildingproject.Idea.domain.Idea;
import com.skhu.gdgocteambuildingproject.global.entity.BaseEntity;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.ScheduleType;
import com.skhu.gdgocteambuildingproject.user.domain.User;
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
    private final List<ProjectAvailablePart> availableParts = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private final List<Idea> ideas = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private final List<ProjectSchedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private final List<ProjectParticipant> participants = new ArrayList<>();

    public List<Part> getAvailableParts() {
        return availableParts.stream()
                .map(ProjectAvailablePart::getPart)
                .toList();
    }

    public boolean isAvailable(Part part) {
        return availableParts.stream()
                .map(ProjectAvailablePart::getPart)
                .anyMatch(availablePart -> availablePart == part);
    }

    public boolean isScheduled() {
        if (schedules.isEmpty()) {
            return false;
        }

        return schedules.stream()
                .allMatch(ProjectSchedule::isScheduled);
    }

    public boolean isUnscheduled() {
        if (schedules.isEmpty()) {
            return true;
        }

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
                    // 발표 일정이 아니라면 confirm할 일이 없어서 true로 초기화
                    .confirmed(!type.isAnnouncement())
                    .build();

            schedules.add(schedule);
        }
    }

    public Optional<ProjectSchedule> getCurrentSchedule() {
        LocalDateTime now = LocalDateTime.now();

        return schedules.stream()
                .filter(ProjectSchedule::isScheduled)
                .filter(schedule -> isCurrentSchedule(now, schedule))
                .findFirst();
    }

    private boolean isCurrentSchedule(LocalDateTime now, ProjectSchedule schedule) {
        if (schedule.getType().isAnnouncement()) {
            return isCurrentAnnouncementSchedule(now, schedule);
        }

        return isCurrentNonAnnouncementSchedule(now, schedule);
    }

    private boolean isCurrentAnnouncementSchedule(LocalDateTime now, ProjectSchedule schedule) {
        if (!now.isAfter(schedule.getStartDate())) {
            return false;
        }

        // 발표 일정의 endDate는, 그 다음 일정의 startDate로 간주함
        LocalDateTime endDate = getNextScheduleStartDate(schedule.getType());

        return endDate != null && now.isBefore(endDate);
    }

    private boolean isCurrentNonAnnouncementSchedule(LocalDateTime now, ProjectSchedule schedule) {
        return now.isAfter(schedule.getStartDate()) && now.isBefore(schedule.getEndDate());
    }

    private LocalDateTime getNextScheduleStartDate(ScheduleType scheduleType) {
        ScheduleType nextScheduleType = scheduleType.getNextScheduleType();
        if (nextScheduleType == null) {
            return null;
        }

        return schedules.stream()
                .filter(schedule -> schedule.getType() == nextScheduleType)
                .map(ProjectSchedule::getStartDate)
                .findAny()
                .orElse(null);
    }

    public Optional<ProjectSchedule> getPreviousScheduleOf(ScheduleType scheduleType) {
        if (scheduleType == null) {
            return Optional.empty();
        }

        ScheduleType prevScheduleType = scheduleType.getPrevScheduleType();

        return getSchedules().stream()
                .filter(schedule -> schedule.getType() == prevScheduleType)
                .findAny();
    }

    public ProjectSchedule getScheduleFrom(ScheduleType scheduleType) {
        return schedules.stream()
                .filter(schedule -> schedule.getType() == scheduleType)
                .findAny()
                .orElseThrow();
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

    public void update(String name, int maxMemberCount, List<Part> availableParts) {
        this.name = name;
        this.maxMemberCount = maxMemberCount;

        this.availableParts.clear();

        if (availableParts != null) {
            for (Part part : availableParts) {
                ProjectAvailablePart projectAvailablePart = ProjectAvailablePart.builder()
                        .part(part)
                        .project(this)
                        .build();

                this.availableParts.add(projectAvailablePart);
            }
        }
    }

    public void participate(User user) {
        validateParticipate(user);

        ProjectParticipant participant = ProjectParticipant.builder()
                .project(this)
                .user(user)
                .build();

        participants.add(participant);
    }

    public void clearParticipants() {
        participants.clear();
    }

    private void validateParticipate(User user) {
        boolean alreadyParticipated = participants.stream()
                .map(ProjectParticipant::getUser)
                .anyMatch(user::equals);

        if (alreadyParticipated) {
            throw new IllegalStateException(ALREADY_PARTICIPATED.getMessage());
        }
    }
}
