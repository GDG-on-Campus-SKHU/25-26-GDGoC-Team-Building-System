package com.skhu.gdgocteambuildingproject.teambuilding.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ProjectScheduleResponseDto(
        String scheduleName,
        LocalDateTime startAt,
        LocalDateTime endAt
) {
}
