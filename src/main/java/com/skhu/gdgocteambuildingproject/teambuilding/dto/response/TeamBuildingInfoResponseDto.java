package com.skhu.gdgocteambuildingproject.teambuilding.dto.response;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import java.util.List;
import lombok.Builder;

@Builder
public record TeamBuildingInfoResponseDto(
        Long projectId,
        String projectName,
        int maxMemberCount,
        boolean registrable,
        boolean canEnroll,
        List<ProjectScheduleResponseDto> schedules,
        List<Part> availableParts
) {
}
