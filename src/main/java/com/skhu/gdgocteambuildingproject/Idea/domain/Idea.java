package com.skhu.gdgocteambuildingproject.Idea.domain;

import com.skhu.gdgocteambuildingproject.Idea.domain.enumtype.IdeaStatus;
import com.skhu.gdgocteambuildingproject.global.entity.BaseEntity;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Idea extends BaseEntity {

    private String topic;
    private String title;
    private String introduction;
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private IdeaStatus registerStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "idea", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<IdeaMemberComposition> memberCompositions = new ArrayList<>();

    @OneToMany(mappedBy = "idea", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<IdeaEnrollment> enrollments = new ArrayList<>();

    @OneToMany(mappedBy = "idea", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<IdeaMember> members = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private TeamBuildingProject project;

    public int getMaxMemberCount() {
        return memberCompositions.stream()
                .mapToInt(IdeaMemberComposition::getCount)
                .sum();
    }

    public int getCurrentMemberCount() {
        return members.size();
    }
}
