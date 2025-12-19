package com.skhu.gdgocteambuildingproject.admin.service;

import com.skhu.gdgocteambuildingproject.admin.dto.dashboard.DashboardSummaryResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.dashboard.ProjectSummaryResponseDto;
import com.skhu.gdgocteambuildingproject.admin.model.DashboardResponseMapper;
import com.skhu.gdgocteambuildingproject.admin.model.ProjectSummaryMapper;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.teambuilding.repository.IdeaMemberRepository;
import com.skhu.gdgocteambuildingproject.teambuilding.repository.TeamBuildingProjectRepository;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.ApprovalStatus;
import com.skhu.gdgocteambuildingproject.user.repository.UserRepository;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final UserRepository userRepository;
    private final TeamBuildingProjectRepository teamBuildingProjectRepository;
    private final IdeaMemberRepository ideaMemberRepository;

    private final ProjectSummaryMapper projectSummaryMapper;
    private final DashboardResponseMapper dashboardResponseMapper;

    @Override
    @Transactional(readOnly = true)
    public DashboardSummaryResponseDto getDashboard() {

        long countByWaitingUser = userRepository.countByApprovalStatus(ApprovalStatus.WAITING);

        long countByApprovalUser = userRepository.countByApprovalStatus(ApprovalStatus.APPROVED);

        List<ProjectSummaryResponseDto> activeProjects = teamBuildingProjectRepository.findAll().stream()
                .map(this::toProjectSummary)
                .filter(Objects::nonNull)
                .toList();

        return dashboardResponseMapper.toDashboardResponse(
                countByWaitingUser,
                countByApprovalUser,
                activeProjects);
    }

    private ProjectSummaryResponseDto toProjectSummary(TeamBuildingProject project) {

        int confirmedMemberCount =
                ideaMemberRepository.countConfirmedMembers(project.getId());

        return projectSummaryMapper.toSummaryResponse(
                project,
                confirmedMemberCount
        );
    }
}
