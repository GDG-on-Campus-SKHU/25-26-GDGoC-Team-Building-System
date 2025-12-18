package com.skhu.gdgocteambuildingproject.teambuilding.dto.idea;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.project.ProjectTopicResponseDto;
import java.util.List;
import lombok.Builder;

@Builder
public record IdeaConfigurationResponseDto(
        List<ProjectTopicResponseDto> topics,
        List<Part> availableParts,
        Integer maxMemberCount
) {
}

