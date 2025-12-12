package com.skhu.gdgocteambuildingproject.teambuilding.domain;


import com.skhu.gdgocteambuildingproject.global.entity.BaseEntity;
import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.ScheduleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_PROJECT_SCHEDULE_TYPE",
                        columnNames = {"project_id", "type"}
                )
        }
)
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectSchedule extends BaseEntity {

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Builder.Default
    private boolean confirmed = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduleType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private TeamBuildingProject project;

    public boolean isIdeaRegistrable() {
        return type == ScheduleType.IDEA_REGISTRATION;
    }

    public boolean isEnrollmentAvailable() {
        return type.isEnrollmentAvailable();
    }

    public boolean isScheduled() {
        if (type.isAnnouncement()) {
            return startDate != null;
        }

        return startDate != null && endDate != null;
    }

    public boolean isUnscheduled() {
        if (type.isAnnouncement()) {
            return startDate == null;
        }

        return startDate == null || endDate == null;
    }

    public void updateDates(
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        validateDates(startDate, endDate);

        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void markAsConfirm() {
        this.confirmed = true;
    }

    private void validateDates(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null) {
            throw new IllegalArgumentException(ExceptionMessage.ILLEGAL_SCHEDULE_DATE.getMessage());
        }

        if (type.isAnnouncement()) {
            validateAnnouncementEndDate(endDate);
        } else {
            validateNonAnnouncementEndDate(startDate, endDate);
        }
    }

    private void validateAnnouncementEndDate(LocalDateTime endDate) {
        if (endDate != null) {
            throw new IllegalArgumentException(ExceptionMessage.ILLEGAL_SCHEDULE_DATE.getMessage());
        }
    }

    private void validateNonAnnouncementEndDate(LocalDateTime startDate, LocalDateTime endDate) {
        if (endDate == null) {
            throw new IllegalArgumentException(ExceptionMessage.ILLEGAL_SCHEDULE_DATE.getMessage());
        }

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(ExceptionMessage.ILLEGAL_SCHEDULE_DATE.getMessage());
        }
    }
}
