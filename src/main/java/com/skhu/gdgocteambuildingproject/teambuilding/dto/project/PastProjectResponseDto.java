package com.skhu.gdgocteambuildingproject.teambuilding.dto.project;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record PastProjectResponseDto(
        long projectId,
        String name,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
