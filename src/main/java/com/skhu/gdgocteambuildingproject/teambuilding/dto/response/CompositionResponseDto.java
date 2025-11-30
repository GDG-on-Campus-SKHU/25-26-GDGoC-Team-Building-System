package com.skhu.gdgocteambuildingproject.teambuilding.dto.response;

import com.skhu.gdgocteambuildingproject.Idea.domain.enumtype.IdeaMemberRole;
import java.util.List;
import lombok.Builder;

@Builder
public record CompositionResponseDto(
        long ideaId,
        String ideaTitle,
        String ideaIntroduction,
        IdeaMemberRole myRole,
        List<CompositionPartResponseDto> compositions
) {
}
