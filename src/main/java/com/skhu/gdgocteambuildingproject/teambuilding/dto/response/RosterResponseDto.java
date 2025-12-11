package com.skhu.gdgocteambuildingproject.teambuilding.dto.response;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.IdeaMemberRole;
import java.util.List;
import lombok.Builder;

@Builder
public record RosterResponseDto(
        long ideaId,
        String ideaTitle,
        String ideaIntroduction,
        IdeaMemberRole myRole,
        List<RosterPartResponseDto> rosters
) {
}
