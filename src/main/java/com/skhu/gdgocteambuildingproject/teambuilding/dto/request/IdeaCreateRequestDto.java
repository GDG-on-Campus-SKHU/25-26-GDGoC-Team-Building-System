package com.skhu.gdgocteambuildingproject.teambuilding.dto.request;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import java.util.List;

public record IdeaCreateRequestDto(
        String title,
        String introduction,
        String description,
        String topic,
        Part creatorPart,
        List<IdeaMemberCompositionRequestDto> compositions
) {
}
