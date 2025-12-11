package com.skhu.gdgocteambuildingproject.admin.dto.idea;

import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.IdeaCreatorInfoResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.IdeaMemberCompositionResponseDto;
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

