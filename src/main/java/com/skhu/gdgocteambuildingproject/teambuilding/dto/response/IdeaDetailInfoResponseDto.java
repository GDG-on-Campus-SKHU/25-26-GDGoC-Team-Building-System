package com.skhu.gdgocteambuildingproject.teambuilding.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record IdeaDetailInfoResponseDto(
        Long ideaId,
        String title,
        String introduction,
        String description,
        IdeaCreatorInfoResponseDto creator,
        List<IdeaMemberCompositionResponseDto> compositions
) {
}
