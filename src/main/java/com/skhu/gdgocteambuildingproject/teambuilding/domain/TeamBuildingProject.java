package com.skhu.gdgocteambuildingproject.teambuilding.domain;

import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.SCHEDULE_ALREADY_INITIALIZED;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.SCHEDULE_NOT_EXIST;
import static java.util.stream.Collectors.*;

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
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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
    private final List<ProjectTopic> topics = new ArrayList<>();

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
                .filter(Objects::nonNull)
                .findAny()
                .orElse(null);
    }

    public LocalDateTime getStartDate() {
        return schedules.stream()
                .filter(schedule -> schedule.getType() == ScheduleType.FIRST_TEAM_BUILDING)
                .map(ProjectSchedule::getStartDate)
                .filter(Objects::nonNull)
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
                .filter(Objects::nonNull)
                .findAny()
                .orElse(null);
    }

    public Optional<ProjectSchedule> getPreviousScheduleOf(ScheduleType scheduleType) {
        if (scheduleType == null) {
            return Optional.empty();
        }

        ScheduleType prevScheduleType = scheduleType.getPrevScheduleType();

        return schedules.stream()
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

    public void update(
            String name,
            int maxMemberCount,
            List<Part> availableParts,
            List<String> topics,
            List<User> participants
    ) {
        this.name = name;
        this.maxMemberCount = maxMemberCount;

        updateAvailableParts(availableParts);
        updateTopics(topics);
        updateParticipants(participants);
    }

    private void updateParticipants(List<User> participants) {
        Set<User> uniqueParticipants = participants.stream()
                .collect(toUnmodifiableSet());
        Set<ProjectParticipant> existingParticipants = this.participants.stream()
                .collect(toUnmodifiableSet());

        removeExcludedParticipants(existingParticipants, uniqueParticipants);
        addNewParticipants(existingParticipants, uniqueParticipants);
    }

    private void updateTopics(List<String> topics) {
        // 중복 제거
        Set<String> uniqueTopics = topics.stream()
                .collect(toUnmodifiableSet());

        Set<ProjectTopic> existingTopics = this.topics.stream()
                .collect(toUnmodifiableSet());

        removeExcludedTopics(existingTopics, uniqueTopics);
        addNewTopics(existingTopics, uniqueTopics);
    }

    private void updateAvailableParts(List<Part> availableParts) {
        Set<Part> uniqueParts = availableParts.stream()
                .collect(toUnmodifiableSet());
        Set<ProjectAvailablePart> existingAvailableParts = this.availableParts.stream()
                .collect(toUnmodifiableSet());

        removeExcludedAvailableParts(existingAvailableParts, uniqueParts);
        addNewAvailableParts(existingAvailableParts, uniqueParts);
    }

    /**
     * 새로운 Part 목록에 없는 기존 AvailablePart를 삭제한다.
     */
    private void removeExcludedAvailableParts(
            Set<ProjectAvailablePart> existingAvailableParts,
            Set<Part> newParts
    ) {
        Set<ProjectAvailablePart> toRemoveAvailableParts = existingAvailableParts.stream()
                .filter(availablePart -> !newParts.contains(availablePart.getPart()))
                .collect(toUnmodifiableSet());

        this.availableParts.removeAll(toRemoveAvailableParts);
    }

    /**
     * 기존 AvailablePart 목록에 없는 새로운 Part를 생성한다.
     */
    private void addNewAvailableParts(
            Set<ProjectAvailablePart> existingAvailableParts,
            Set<Part> newParts
    ) {
        Set<Part> existingParts = existingAvailableParts.stream()
                .map(ProjectAvailablePart::getPart)
                .collect(toUnmodifiableSet());

        Set<Part> toAddParts = newParts.stream()
                .filter(part -> !existingParts.contains(part))
                .collect(toUnmodifiableSet());

        for (Part part : toAddParts) {
            ProjectAvailablePart projectAvailablePart = ProjectAvailablePart.builder()
                    .part(part)
                    .project(this)
                    .build();

            this.availableParts.add(projectAvailablePart);
        }
    }

    /**
     * 새로운 Topic 목록에 없는 기존 Topic을 삭제한다.
     */
    private void removeExcludedTopics(
            Set<ProjectTopic> existingTopics,
            Set<String> newTopics
    ) {
        Set<ProjectTopic> toRemoveTopics = existingTopics.stream()
                .filter(topic -> !newTopics.contains(topic.getTopic()))
                .collect(toUnmodifiableSet());

        this.topics.removeAll(toRemoveTopics);
    }

    /**
     * 기존 Topic 목록에 없는 새로운 Topic을 생성한다.
     */
    private void addNewTopics(
            Set<ProjectTopic> existingTopics,
            Set<String> newTopics
    ) {
        Set<String> existingTopicStrings = existingTopics.stream()
                .map(ProjectTopic::getTopic)
                .collect(toUnmodifiableSet());

        Set<String> toAddTopics = newTopics.stream()
                .filter(topic -> !existingTopicStrings.contains(topic))
                .collect(toUnmodifiableSet());

        for (String topic : toAddTopics) {
            ProjectTopic projectTopic = ProjectTopic.builder()
                    .topic(topic)
                    .project(this)
                    .build();

            this.topics.add(projectTopic);
        }
    }

    /**
     * 새로운 참여자 목록에 없는 기존 참여자를 삭제한다.
     */
    private void removeExcludedParticipants(
            Set<ProjectParticipant> existingParticipants,
            Set<User> newParticipatedUsers
    ) {
        Set<ProjectParticipant> toRemoveParticipants = existingParticipants.stream()
                .filter(participant -> !newParticipatedUsers.contains(participant.getUser()))
                .collect(toUnmodifiableSet());

        this.participants.removeAll(toRemoveParticipants);
    }

    /**
     * 기존 참여자 목록에 없는 새로운 참여자를 생성한다.
     */
    private void addNewParticipants(Set<ProjectParticipant> existingParticipants, Set<User> newParticipatedUsers) {
        Set<User> existingParticipatedUsers = existingParticipants.stream()
                .map(ProjectParticipant::getUser)
                .collect(toUnmodifiableSet());

        Set<User> toAddUsers = newParticipatedUsers.stream()
                .filter(user -> !existingParticipatedUsers.contains(user))
                .collect(toUnmodifiableSet());

        for (User user : toAddUsers) {
            ProjectParticipant participant = ProjectParticipant.builder()
                    .project(this)
                    .user(user)
                    .build();

            this.participants.add(participant);
        }
    }
}
