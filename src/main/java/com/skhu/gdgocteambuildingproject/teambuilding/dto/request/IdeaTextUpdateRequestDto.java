package com.skhu.gdgocteambuildingproject.teambuilding.dto.request;

import java.util.List;

public record IdeaTextUpdateRequestDto(
        String title,
        String introduction,
        String description,
        String topic
) {
    public List<String> getTexts() {
        return List.of(title, introduction, description, topic);
    }
}
