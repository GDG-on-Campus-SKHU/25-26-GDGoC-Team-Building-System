package com.skhu.gdgocteambuildingproject.teambuilding.domain;

import com.skhu.gdgocteambuildingproject.global.entity.BaseEntity;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import jakarta.persistence.Entity;
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
                        name = "UK_PROJECT_USER",
                        columnNames = {"project_id", "user_id"}
                )
        }
)
public class ProjectParticipant extends BaseEntity {
    @ManyToOne
    @JoinColumn(nullable = false)
    private TeamBuildingProject project;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;
}
