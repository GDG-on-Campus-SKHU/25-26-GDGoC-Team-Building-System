package com.skhu.gdgocteambuildingproject.teambuilding.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skhu.gdgocteambuildingproject.Idea.domain.enumtype.IdeaStatus;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import java.util.List;

public record IdeaCreateRequestDto(
        String title,
        String introduction,
        String description,
        Long topicId,
        Part creatorPart,
        IdeaStatus registerStatus,
        List<IdeaMemberCompositionRequestDto> compositions
) {
    @JsonIgnore
    public List<String> getTexts() {
        return List.of(title, introduction, description);
    }
}
