package com.skhu.gdgocteambuildingproject.teambuilding.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record PastProjectResponseDto(
        String name,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
