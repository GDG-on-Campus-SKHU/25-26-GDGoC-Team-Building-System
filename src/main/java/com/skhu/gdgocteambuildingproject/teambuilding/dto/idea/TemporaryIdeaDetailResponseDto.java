package com.skhu.gdgocteambuildingproject.teambuilding.dto.idea;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record TemporaryIdeaDetailResponseDto(
        LocalDateTime lastTemporarySavedAt,
        IdeaDetailInfoResponseDto idea
) {
}
