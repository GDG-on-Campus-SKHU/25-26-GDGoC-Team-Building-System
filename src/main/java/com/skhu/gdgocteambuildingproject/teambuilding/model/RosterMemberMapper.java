package com.skhu.gdgocteambuildingproject.teambuilding.model;

import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.ILLEGAL_ENROLLMENT_STATUS;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.NOT_MEMBER_OF_IDEA;

import com.skhu.gdgocteambuildingproject.Idea.domain.IdeaEnrollment;
import com.skhu.gdgocteambuildingproject.Idea.domain.IdeaMember;
import com.skhu.gdgocteambuildingproject.Idea.domain.enumtype.IdeaMemberRole;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.RosterMemberResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class RosterMemberMapper {

    public RosterMemberResponseDto map(IdeaEnrollment enrollment) {
        return switch (enrollment.getStatus()) {
            case ACCEPTED -> mapAcceptedEnrollment(enrollment);
            case SCHEDULED_TO_ACCEPT -> mapScheduledToAcceptEnrollment(enrollment.getApplicant());
            default -> throw new IllegalStateException(ILLEGAL_ENROLLMENT_STATUS.getMessage());
        };
    }

    private RosterMemberResponseDto mapAcceptedEnrollment(IdeaEnrollment enrollment) {
        User applicant = enrollment.getApplicant();
        IdeaMember member = findMemberFromEnrollment(enrollment);

        return RosterMemberResponseDto.builder()
                .userId(applicant.getId())
                .memberName(applicant.getName())
                .memberRole(member.getRole())
                .deletable(false) // 수락된 멤버는 삭제 불가능
                .build();
    }

    private RosterMemberResponseDto mapScheduledToAcceptEnrollment(User applicant) {
        return RosterMemberResponseDto.builder()
                .userId(applicant.getId())
                .memberName(applicant.getName())
                .memberRole(IdeaMemberRole.MEMBER)
                .deletable(true) // 수락 예정 멤버만 삭제 가능
                .build();
    }

    private IdeaMember findMemberFromEnrollment(IdeaEnrollment enrollment) {
        return enrollment.getIdea().getMembers().stream()
                .filter(member -> member.getUser().equals(enrollment.getApplicant()))
                .findAny()
                .orElseThrow(() -> new IllegalStateException(NOT_MEMBER_OF_IDEA.getMessage()));
    }
}

