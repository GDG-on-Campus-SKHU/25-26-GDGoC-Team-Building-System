package com.skhu.gdgocteambuildingproject.teambuilding.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

public record IdeaTextUpdateRequestDto(
        String title,
        String introduction,
        String description,
        String topic
) {
    @JsonIgnore
    public List<String> getTexts() {
        return List.of(title, introduction, description, topic);
    }
}
