package com.skhu.gdgocteambuildingproject.user.domain;

import com.skhu.gdgocteambuildingproject.Idea.domain.Idea;
import com.skhu.gdgocteambuildingproject.Idea.domain.IdeaEnrollment;
import com.skhu.gdgocteambuildingproject.auth.domain.RefreshToken;
import com.skhu.gdgocteambuildingproject.global.entity.BaseEntity;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectSchedule;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.Choice;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.ApprovalStatus;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserPosition;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserRole;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseEntity {

    private String email;
    private String password;

    private String name;
    private String number;
    private String introduction;
    private String school;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private UserPosition position;

    @Enumerated(EnumType.STRING)
    private Part part;
    private String generation;

    private boolean deleted;
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RefreshToken> refreshTokens = new ArrayList<>();

    // 승인 여부
    @Column(nullable = false)
    private ApprovalStatus approvalStatus = ApprovalStatus.WAITING;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TechStack> techStacks = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Grade> grades = new ArrayList<>();

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Idea> ideas = new ArrayList<>();

    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<IdeaEnrollment> enrollments = new ArrayList<>();

    @Builder
    public User(String email, String password, String name, String number,
                String introduction, String school, UserRole role, UserPosition position,
                Part part, String generation) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.number = number;
        this.introduction = introduction;
        this.school = school;
        this.role = role;
        this.position = position;
        this.part = part;
        this.generation = generation;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void approve() {
        this.approvalStatus = ApprovalStatus.APPROVED;
    }

    public void reject() {
        this.approvalStatus = ApprovalStatus.REJECTED;
    }

    public boolean isChoiceAvailable(ProjectSchedule schedule, Choice choice) {
        return enrollments.stream()
                .filter(enrollment -> enrollment.getSchedule().equals(schedule))
                .noneMatch(enrollment -> enrollment.getChoice().equals(choice));
    }

    /**
     * User - Idea 연관관계 편의 메서드
     */
    public void addIdea(Idea idea) {
        ideas.add(idea);
        idea.setCreator(this);
    }

    public void softDelete() {
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
        this.email = null;
        this.number = null;
    }
}
