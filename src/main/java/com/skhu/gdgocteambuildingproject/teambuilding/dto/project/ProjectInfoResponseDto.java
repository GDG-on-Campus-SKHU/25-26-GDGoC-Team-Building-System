package com.skhu.gdgocteambuildingproject.teambuilding.dto.project;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ProjectInfoResponseDto(
        long projectId,
        String projectName,
        LocalDateTime startAt,
        LocalDateTime endAt
) {
}
