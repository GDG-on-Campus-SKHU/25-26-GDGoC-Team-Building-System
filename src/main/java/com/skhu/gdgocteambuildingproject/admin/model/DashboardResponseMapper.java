package com.skhu.gdgocteambuildingproject.admin.model;

import com.skhu.gdgocteambuildingproject.admin.dto.dashboard.DashboardSummaryResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.dashboard.ProjectSummaryResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DashboardResponseMapper {
    public DashboardSummaryResponseDto toDashboardResponse(
            long waitingUserCount,
            long approvedUserCount,
            List<ProjectSummaryResponseDto> activeProjects
    ) {
        return DashboardSummaryResponseDto.builder()
                .waitingUserCount(waitingUserCount)
                .approvedUserCount(approvedUserCount)
                .activeProjects(activeProjects)
                .build();
    }
}
