package com.skhu.gdgocteambuildingproject.teambuilding.domain;


import com.skhu.gdgocteambuildingproject.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
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
public class ProjectSchedule extends BaseEntity {

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private TeamBuildingProject project;

    public boolean scheduledStartDate() {
        return startDate != null;
    }

    public boolean scheduledEndDate() {
        return endDate != null;
    }
}
