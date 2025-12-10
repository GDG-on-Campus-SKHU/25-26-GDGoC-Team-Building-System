package com.skhu.gdgocteambuildingproject.user.domain;

import com.skhu.gdgocteambuildingproject.Idea.domain.Idea;
import com.skhu.gdgocteambuildingproject.Idea.domain.IdeaEnrollment;
import com.skhu.gdgocteambuildingproject.Idea.domain.IdeaMember;
import com.skhu.gdgocteambuildingproject.auth.domain.RefreshToken;
import com.skhu.gdgocteambuildingproject.global.entity.BaseEntity;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectSchedule;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.Choice;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.ApprovalStatus;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserRole;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import java.util.*;

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
    private Part part;

    private boolean deleted;
    private LocalDate deletedAt;

    private LocalDate approvedAt;

    private LocalDate bannedAt;

    private LocalDate unbannedAt;

    private String banReason;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RefreshToken> refreshTokens = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserGeneration> generation = new HashSet<>();

    // 승인 여부
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus = ApprovalStatus.WAITING;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus = UserStatus.ACTIVE;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<TechStack> techStacks = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Grade> grades = new ArrayList<>();

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Idea> ideas = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<IdeaMember> members = new ArrayList<>();

    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<IdeaEnrollment> enrollments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<UserLink> userLinks = new ArrayList<>();

    @Builder
    public User(String email, String password, String name, String number,
                String introduction, String school, UserRole role,
                Part part) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.number = number;
        this.introduction = introduction;
        this.school = school;
        this.role = role;
        this.part = part;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void approve() {
        this.approvalStatus = ApprovalStatus.APPROVED;
        this.approvedAt = LocalDate.now();
    }

    public void reject() {
        this.approvalStatus = ApprovalStatus.REJECTED;
    }

    public void resetToWaiting() {
        this.approvalStatus = ApprovalStatus.WAITING;
    }

    public boolean isMemberOf(TeamBuildingProject project) {
        return members.stream()
                .map(IdeaMember::getIdea)
                .anyMatch(idea -> project.equals(idea.getProject()));
    }

    public boolean isChoiceAvailable(ProjectSchedule schedule, Choice choice) {
        return enrollments.stream()
                .filter(enrollment -> enrollment.getSchedule().equals(schedule))
                .noneMatch(enrollment -> enrollment.getChoice().equals(choice));
    }

    public Optional<Idea> getIdeaFrom(TeamBuildingProject project) {
        return members.stream()
                .map(IdeaMember::getIdea)
                .filter(idea -> project.equals(idea.getProject()))
                .findAny();
    }

    public List<IdeaEnrollment> getEnrollmentFrom(ProjectSchedule schedule) {
        return enrollments.stream()
                .filter(enrollment -> enrollment.getSchedule().equals(schedule))
                .toList();
    }

    public void updateUserIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void updateTechStacks(List<TechStack> newTechStacks) {
        this.techStacks.clear();
        this.techStacks.addAll(newTechStacks);
    }

    public void updateUserLinks(List<UserLink> newUserLinks) {
        this.userLinks.clear();
        this.userLinks.addAll(newUserLinks);
    }

    public void addIdea(Idea idea) {
        ideas.add(idea);
    }

    public void removeIdea(Idea idea) {
        ideas.remove(idea);
    }

    public void addEnrollment(IdeaEnrollment enrollment) {
        enrollments.add(enrollment);
    }

    public void removeEnrollment(IdeaEnrollment enrollment) {
        enrollments.remove(enrollment);
    }

    public void softDelete() {
        this.deleted = true;
        this.deletedAt = LocalDate.now();
        this.email = null;
        this.number = null;
    }

    public void ban(String reason) {
        this.userStatus = UserStatus.BANNED;
        this.banReason = reason;
        this.bannedAt = LocalDate.now();
    }

    public void unban() {
        this.userStatus = UserStatus.ACTIVE;
        this.banReason = null;
        this.unbannedAt = LocalDate.now();
    }

    public void addGeneration(UserGeneration userGeneration) {
        generation.add(userGeneration);
    }

    public void updateSchool(String school) {
        this.school = school;
    }

    public void updatePart(Part part) {
        this.part = part;
    }
}
