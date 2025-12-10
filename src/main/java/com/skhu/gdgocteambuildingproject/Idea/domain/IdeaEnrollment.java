package com.skhu.gdgocteambuildingproject.Idea.domain;

import static com.skhu.gdgocteambuildingproject.Idea.domain.enumtype.EnrollmentStatus.ACCEPTED;
import static com.skhu.gdgocteambuildingproject.Idea.domain.enumtype.EnrollmentStatus.EXPIRED;
import static com.skhu.gdgocteambuildingproject.Idea.domain.enumtype.EnrollmentStatus.REJECTED;
import static com.skhu.gdgocteambuildingproject.Idea.domain.enumtype.EnrollmentStatus.SCHEDULED_TO_ACCEPT;
import static com.skhu.gdgocteambuildingproject.Idea.domain.enumtype.EnrollmentStatus.SCHEDULED_TO_REJECT;
import static com.skhu.gdgocteambuildingproject.Idea.domain.enumtype.EnrollmentStatus.WAITING;

import com.skhu.gdgocteambuildingproject.Idea.domain.enumtype.EnrollmentStatus;
import com.skhu.gdgocteambuildingproject.global.entity.BaseEntity;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectSchedule;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.Choice;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_USER_CHOICE_SCHEDULE",
                        columnNames = {"user_id", "choice", "schedule_id"}
                )
        }
)
public class IdeaEnrollment extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Choice choice;

    @Column(nullable = false)
    private Part part;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EnrollmentStatus status = WAITING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Idea idea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User applicant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private ProjectSchedule schedule;

    public void confirmStatus() {
        switch (status) {
            case SCHEDULED_TO_ACCEPT -> status = ACCEPTED;
            case SCHEDULED_TO_REJECT -> status = REJECTED;
            case WAITING -> status = EXPIRED;
        }
    }

    /**
     * 지원의 상태를 '수락'으로 전이한다.
     */
    public void accept() {
        status = ACCEPTED;
    }

    public boolean isCancelable() {
        return status.isWaitingToConfirm();
    }

    public boolean isScheduledToAccept() {
        return status == SCHEDULED_TO_ACCEPT;
    }

    public void scheduleToAccept() {
        status = SCHEDULED_TO_ACCEPT;
    }

    public void scheduleToReject() {
        status = SCHEDULED_TO_REJECT;
    }

    /**
     * 지원 상태를 수락됨(ACCEPTED)으로 변경한다.
     */
    public void accept() {
        status = ACCEPTED;
    }
}
