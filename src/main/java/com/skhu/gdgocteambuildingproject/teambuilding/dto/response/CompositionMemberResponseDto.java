package com.skhu.gdgocteambuildingproject.teambuilding.dto.response;

import com.skhu.gdgocteambuildingproject.Idea.domain.IdeaMember;
import com.skhu.gdgocteambuildingproject.Idea.domain.enumtype.IdeaMemberRole;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import lombok.Builder;

@Builder
public record CompositionMemberResponseDto(
        long memberId,
        String memberName,
        IdeaMemberRole memberRole
) {
    public static CompositionMemberResponseDto from(IdeaMember member) {
        User user = member.getUser();

        return CompositionMemberResponseDto.builder()
                .memberId(member.getId())
                .memberName(user.getName())
                .memberRole(member.getRole())
                .build();
    }
}
