package com.skhu.gdgocteambuildingproject.teambuilding.model;

import static com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.EnrollmentStatus.SCHEDULED_TO_ACCEPT;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.ILLEGAL_ENROLLMENT_STATUS;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.IdeaEnrollment;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.IdeaMember;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.IdeaMemberRole;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.RosterMemberResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class RosterMemberMapper {

    /**
     * 멤버를 DTO로 매핑한다.
     */
    public RosterMemberResponseDto map(IdeaMember member) {
        User user = member.getUser();

        return RosterMemberResponseDto.builder()
                .userId(user.getId())
                .memberName(user.getName())
                .memberRole(member.getRole())
                .confirmed(true)
                .build();
    }

    /**
     * 수락 예정인 지원을 DTO로 매핑한다.
     */
    public RosterMemberResponseDto map(IdeaEnrollment enrollment) {
        validateEnrollmentStatus(enrollment);

        User applicant = enrollment.getApplicant();

        return RosterMemberResponseDto.builder()
                .userId(applicant.getId())
                .memberName(applicant.getName())
                .memberRole(IdeaMemberRole.MEMBER)
                .confirmed(false)
                .build();
    }

    private void validateEnrollmentStatus(IdeaEnrollment enrollment) {
        if (enrollment.getStatus() != SCHEDULED_TO_ACCEPT) {
            throw new IllegalStateException(ILLEGAL_ENROLLMENT_STATUS.getMessage());
        }
    }
}

