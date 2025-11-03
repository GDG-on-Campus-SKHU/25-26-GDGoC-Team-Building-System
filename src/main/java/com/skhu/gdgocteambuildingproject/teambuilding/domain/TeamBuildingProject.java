package com.skhu.gdgocteambuildingproject.teambuilding.domain;

import com.skhu.gdgocteambuildingproject.Idea.domain.Idea;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamBuildingProject {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int maxMemberCount;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Idea> ideas = new ArrayList<>();

    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL)
    private ProjectSchedule schedule;
}