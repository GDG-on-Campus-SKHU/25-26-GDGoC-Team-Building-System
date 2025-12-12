package com.skhu.gdgocteambuildingproject.teambuilding.dto.project;

import lombok.Builder;

@Builder
public record ProjectTopicResponseDto(
        Long topicId,
        String topic
) {
}
