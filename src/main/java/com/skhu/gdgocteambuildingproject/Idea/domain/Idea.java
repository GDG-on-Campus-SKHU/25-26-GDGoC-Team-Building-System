package com.skhu.gdgocteambuildingproject.Idea.domain;

import com.skhu.gdgocteambuildingproject.Idea.domain.enumtype.IdeaStatus;
import com.skhu.gdgocteambuildingproject.global.entity.BaseEntity;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import jakarta.persistence.*;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Idea extends BaseEntity {

    private static final BiFunction<Part, Integer, Integer> INCREASE_COUNT = (part, prevCount) -> prevCount + 1;

    private String topic;
    private String title;
    private String introduction;
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private IdeaStatus registerStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private User creator;

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

    public Map<Part, Integer> getMaxMemberCountsByPart() {
        return memberCompositions.stream()
                .collect(Collectors.toMap(
                        IdeaMemberComposition::getPart,
                        IdeaMemberComposition::getCount
                ));
    }

    public Map<Part, Integer> getCurrentMemberCountsByPart() {
        Map<Part, Integer> currentCounts = initCurrentCounts();

        for (IdeaMember member : members) {
            Part part = member.getPart();
            currentCounts.compute(part, INCREASE_COUNT);
        }

        return currentCounts;
    }

    private Map<Part, Integer> initCurrentCounts() {
        EnumMap<Part, Integer> partMap = new EnumMap<>(Part.class);

        for (Part part : Part.values()) {
            partMap.put(part, 0);
        }

        return partMap;
    }
}
