package com.skhu.gdgocteambuildingproject.teambuilding.model;

import com.skhu.gdgocteambuildingproject.Idea.domain.Idea;
import com.skhu.gdgocteambuildingproject.admin.dto.project.ProjectTotalResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.TeamBuildingInfoResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamBuildingInfoMapper {
    private final ProjectTotalMapper projectTotalMapper;

    public TeamBuildingInfoResponseDto map(TeamBuildingProject project, User user) {
        ProjectTotalResponseDto projectTotal = projectTotalMapper.map(project);

        return TeamBuildingInfoResponseDto.builder()
                .project(projectTotal)
                .registrable(isRegistrable(project, user))
                .canEnroll(isCanEnroll(project, user))
                .build();
    }

    private boolean isRegistrable(TeamBuildingProject project, User user) {
        return user.getIdeas().stream()
                .filter(idea -> idea.getProject() == project)
                .noneMatch(Idea::isRegistered);
    }

    private boolean isCanEnroll(TeamBuildingProject project, User user) {
        return !user.isMemberOf(project);
    }
}
