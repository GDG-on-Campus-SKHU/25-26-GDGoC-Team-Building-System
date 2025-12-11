package com.skhu.gdgocteambuildingproject.teambuilding.dto.idea;

import java.util.List;
import lombok.Builder;

@Builder
public record AdminIdeaDetailResponseDto(
        Long ideaId,
        String title,
        String introduction,
        String description,
        Long topicId,
        String topic,
        IdeaCreatorInfoResponseDto creator,
        boolean deleted,
        List<IdeaMemberCompositionResponseDto> compositions
) {
}

