package com.skhu.gdgocteambuildingproject.teambuilding.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import java.util.List;

public record IdeaUpdateRequestDto(
        String title,
        String introduction,
        String description,
        Long topicId,
        Part creatorPart,
        List<IdeaMemberCompositionRequestDto> compositions
) {
    @JsonIgnore
    public List<String> getTexts() {
        return List.of(title, introduction, description);
    }
}
