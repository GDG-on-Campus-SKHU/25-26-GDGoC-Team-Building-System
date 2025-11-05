package com.skhu.gdgocteambuildingproject.admin.domain;

import com.skhu.gdgocteambuildingproject.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DashboardStats extends BaseEntity {

    private int totalUsers;
    private int activeProjects;
    private int ongoingTeamBuildings;
}