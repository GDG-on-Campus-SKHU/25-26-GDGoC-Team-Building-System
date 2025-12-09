package com.skhu.gdgocteambuildingproject.admin.dto.idea;

import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.IdeaCreatorInfoResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.IdeaMemberCompositionResponseDto;
import java.util.List;
import lombok.Builder;

@Builder
public record AdminIdeaDetailResponseDto(
        Long ideaId,
        String title,
        String introduction,
        String description,
        IdeaCreatorInfoResponseDto creator,
        boolean deleted,
        List<IdeaMemberCompositionResponseDto> compositions
) {
}

