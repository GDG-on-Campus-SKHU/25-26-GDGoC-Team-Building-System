package com.skhu.gdgocteambuildingproject.teambuilding.dto.response;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.IdeaMemberRole;
import lombok.Builder;

@Builder
public record RosterMemberResponseDto(
        long userId,
        String memberName,
        IdeaMemberRole memberRole,
        boolean confirmed
) {
}

