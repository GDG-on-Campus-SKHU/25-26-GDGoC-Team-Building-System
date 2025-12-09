package com.skhu.gdgocteambuildingproject.teambuilding.model;

import com.skhu.gdgocteambuildingproject.Idea.domain.IdeaMember;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.RosterMemberResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class RosterMemberMapper {

    public RosterMemberResponseDto map(IdeaMember member) {
        User user = member.getUser();

        return RosterMemberResponseDto.builder()
                .memberId(member.getId())
                .memberName(user.getName())
                .memberRole(member.getRole())
                .deletable(isDeletable(member))
                .build();
    }

    private boolean isDeletable(IdeaMember member) {
        // 팀장은 삭제 불가능, 멤버는 삭제 가능
        return member.isMember();
    }
}

