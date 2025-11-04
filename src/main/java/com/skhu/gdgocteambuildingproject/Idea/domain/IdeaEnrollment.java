package com.skhu.gdgocteambuildingproject.Idea.domain;

import com.skhu.gdgocteambuildingproject.Idea.domain.enumtype.EnrollmentStatus;
import com.skhu.gdgocteambuildingproject.global.entity.BaseEntity;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IdeaEnrollment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String choice;
    private String part;
    private String phase;

    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Idea idea;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private TeamBuildingProject project;
}