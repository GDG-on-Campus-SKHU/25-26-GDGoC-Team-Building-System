package com.skhu.gdgocteambuildingproject.admin.dto.dashboard;

import lombok.Builder;

import java.util.List;

@Builder
public record DashboardSummaryResponseDto(
        long waitingUserCount,
        long approvedUserCount,
        List<ProjectSummaryResponseDto> activeProjects
) {
}
