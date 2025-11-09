package com.skhu.gdgocteambuildingproject.teambuilding.domain;


import com.skhu.gdgocteambuildingproject.global.entity.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectSchedule extends BaseEntity {

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private TeamBuildingProject project;
}
