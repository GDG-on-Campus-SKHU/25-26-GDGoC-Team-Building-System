package com.skhu.gdgocteambuildingproject.Idea.domain;

import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.ENROLLMENT_FOR_OTHER_IDEA;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.ENROLLMENT_NOT_AVAILABLE;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.ILLEGAL_ENROLLMENT_STATUS;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.PART_NOT_AVAILABLE;

import com.skhu.gdgocteambuildingproject.Idea.domain.enumtype.EnrollmentStatus;
import com.skhu.gdgocteambuildingproject.Idea.domain.enumtype.IdeaMemberRole;
import com.skhu.gdgocteambuildingproject.Idea.domain.enumtype.IdeaStatus;
import com.skhu.gdgocteambuildingproject.global.entity.BaseEntity;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectSchedule;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted = false")
public class Idea extends BaseEntity {

    private static final BiFunction<Part, Integer, Integer> INCREASE_COUNT = (part, prevCount) -> prevCount + 1;

    private String topic;
    private String title;
    private String introduction;
    private String description;
    // TODO: 멤버를 제거할 때 마다 true로 상태 전이
    @Builder.Default
    private boolean recruiting = true;
    @Builder.Default
    private boolean deleted = false;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Part creatorPart;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private IdeaStatus registerStatus;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User creator;

    @OneToMany(mappedBy = "idea", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private final List<IdeaMemberComposition> memberCompositions = new ArrayList<>();

    @OneToMany(mappedBy = "idea", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private final List<IdeaEnrollment> enrollments = new ArrayList<>();

    @OneToMany(mappedBy = "idea", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private final List<IdeaMember> members = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private TeamBuildingProject project;

    public void initCreatorToMember(Part creatorPart) {
        IdeaMember member = IdeaMember.builder()
                .idea(this)
                .user(creator)
                .part(creatorPart)
                .role(IdeaMemberRole.CREATOR)
                .build();

        members.add(member);
    }

    public void updateCreatorPart(Part part) {
        creatorPart = part;

        getCreatorInMembers().ifPresentOrElse(
                creator -> creator.setPart(part),
                () -> initCreatorToMember(part)
        );
    }

    public void addEnrollment(IdeaEnrollment enrollment) {
        enrollments.add(enrollment);
    }

    /**
     * 지원을 수락 예정 상태로 만든다.
     */
    public void acceptEnrollment(IdeaEnrollment enrollment) {
        validateContains(enrollment);
        validateEnrollmentStatus(enrollment);
        validateMemberCount(enrollment.getPart());

        enrollment.scheduleToAccept();
    }

    /**
     * 지원을 거절 예정 상태로 만든다.
     */
    public void rejectEnrollment(IdeaEnrollment enrollment) {
        validateContains(enrollment);
        validateEnrollmentStatus(enrollment);

        enrollment.scheduleToReject();
    }

    /**
     * 수락/거절 예정인 지원을 수락/거절 상태로 만든다.
     * 수락한 지원자들을 멤버로 추가한다.
     */
    public void confirmEnrollments() {
        List<IdeaEnrollment> confirmableEnrollments = enrollments.stream()
                .filter(IdeaEnrollment::isConfirmable)
                .toList();

        for (IdeaEnrollment enrollment : confirmableEnrollments) {
            if (enrollment.isScheduledToAccept()) {
                enrollment.accept();

                IdeaMember member = createMember(enrollment);
                members.add(member);
            }
            if (enrollment.isScheduledToReject()) {
                enrollment.reject();
            }
        }
    }

    public void updateTexts(
            String topic,
            String title,
            String introduction,
            String description
    ) {
        this.topic = topic;
        this.title = title;
        this.introduction = introduction;
        this.description = description;
    }

    public void updateComposition(Part part, int count) {
        validatePartAvailable(part);

        memberCompositions.stream()
                .filter(composition -> composition.getPart() == part)
                .findAny()
                .ifPresentOrElse(
                        composition -> composition.setCount(count),
                        () -> createComposition(part, count)
                );
    }

    public void register() {
        this.registerStatus = IdeaStatus.REGISTERED;
    }

    public void delete() {
        deleted = true;

        enrollments.clear();
        members.clear();
    }

    public void restore() {
        deleted = false;
        recruiting = true;

        initCreatorToMember(creatorPart);
    }

    public List<Part> getAvailableParts() {
        return project.getAvailableParts();
    }

    public List<IdeaEnrollment> getEnrollmentsOf(ProjectSchedule schedule) {
        return enrollments.stream()
                .filter(enrollment -> enrollment.getSchedule().equals(schedule))
                .toList();
    }

    public List<IdeaMember> getMembersOf(Part part) {
        return members.stream()
                .filter(member -> part.equals(member.getPart()))
                .toList();
    }

    public int getMaxMemberCount() {
        return memberCompositions.stream()
                .mapToInt(IdeaMemberComposition::getCount)
                .sum();
    }

    public int getMaxMemberCountOf(Part part) {
        return memberCompositions.stream()
                .filter(composition -> composition.getPart() == part)
                .map(IdeaMemberComposition::getCount)
                .findAny()
                .orElse(0);
    }

    public int getCurrentMemberCount() {
        return members.size();
    }

    /**
     * 수락 확정인 멤버만 포함한 현재 인원수
     */
    public int getCurrentMemberCountOf(Part part) {
        return (int) members.stream()
                .filter(member -> member.getPart() == part)
                .count();
    }

    /**
     * 수락 예정인 멤버를 포함한 현재 인원수
     */
    public int getCurrentMemberCountIncludeNotConfirm(Part part) {
        int confirmedMemberCount = getCurrentMemberCountOf(part);

        int notConfirmedMemberCount = (int) enrollments.stream()
                .filter(member -> member.getPart() == part)
                .filter(member -> member.getStatus() == EnrollmentStatus.SCHEDULED_TO_ACCEPT)
                .count();

        return confirmedMemberCount + notConfirmedMemberCount;
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

    public boolean hasMember() {
        return members.stream()
                .anyMatch(IdeaMember::isMember);
    }

    public boolean isRegistered() {
        return registerStatus == IdeaStatus.REGISTERED;
    }

    public boolean isTemporary() {
        return registerStatus == IdeaStatus.TEMPORARY;
    }

    public boolean isEnrollmentAvailable(Part part) {
        long currentCount = members.stream()
                .filter(member -> member.getPart() == part)
                .count();

        Integer maxMemberCount = memberCompositions.stream()
                .filter(composition -> composition.getPart() == part)
                .map(IdeaMemberComposition::getCount)
                .findAny()
                .orElse(0);

        return currentCount < maxMemberCount;
    }

    public void removeEnrollment(IdeaEnrollment enrollment) {
        enrollments.remove(enrollment);
    }

    private Map<Part, Integer> initCurrentCounts() {
        EnumMap<Part, Integer> partMap = new EnumMap<>(Part.class);

        for (Part part : Part.values()) {
            partMap.put(part, 0);
        }

        return partMap;
    }

    private void createComposition(Part part, int count) {
        validatePartAvailable(part);

        IdeaMemberComposition composition = IdeaMemberComposition.builder()
                .part(part)
                .count(count)
                .idea(this)
                .build();

        memberCompositions.add(composition);
    }

    private IdeaMember createMember(IdeaEnrollment enrollment) {
        return IdeaMember.builder()
                .idea(this)
                .user(enrollment.getApplicant())
                .part(enrollment.getPart())
                .role(IdeaMemberRole.MEMBER)
                .build();
    }

    private Optional<IdeaMember> getCreatorInMembers() {
        return members.stream()
                .filter(IdeaMember::isCreator)
                .findAny();
    }

    private void validateContains(IdeaEnrollment enrollment) {
        if (enrollment.getIdea().equals(this)) {
            return;
        }

        throw new IllegalStateException(ENROLLMENT_FOR_OTHER_IDEA.getMessage());
    }

    private void validateEnrollmentStatus(IdeaEnrollment enrollment) {
        if (enrollment.getStatus() != EnrollmentStatus.WAITING) {
            throw new IllegalStateException(ILLEGAL_ENROLLMENT_STATUS.getMessage());
        }
    }

    private void validateMemberCount(Part part) {
        int maxMemberCount = getMaxMemberCountOf(part);
        int currentMemberCount = getCurrentMemberCountIncludeNotConfirm(part);

        if (currentMemberCount >= maxMemberCount) {
            throw new IllegalStateException(ENROLLMENT_NOT_AVAILABLE.getMessage());
        }
    }

    private void validatePartAvailable(Part part) {
        boolean isAvailable = project.getAvailableParts().stream()
                .anyMatch(availablePart -> availablePart == part);

        if (!isAvailable) {
            throw new IllegalArgumentException(PART_NOT_AVAILABLE.getMessage());
        }
    }
}
