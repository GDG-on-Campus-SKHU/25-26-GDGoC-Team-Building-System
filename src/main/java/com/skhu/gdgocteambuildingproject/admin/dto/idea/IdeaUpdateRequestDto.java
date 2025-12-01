package com.skhu.gdgocteambuildingproject.admin.dto.idea;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.request.IdeaMemberCompositionRequestDto;
import java.util.List;

public record IdeaUpdateRequestDto(
        String title,
        String introduction,
        String description,
        String topic,
        Part creatorPart,
        List<IdeaMemberCompositionRequestDto> compositions
) {
    public List<String> getTexts() {
        return List.of(title, introduction, description, topic);
    }
}
