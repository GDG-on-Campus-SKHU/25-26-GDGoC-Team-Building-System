package com.skhu.gdgocteambuildingproject.teambuilding.model.mapper;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.Idea;
import com.skhu.gdgocteambuildingproject.admin.dto.project.ProjectTotalResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectSchedule;
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
        return isRegistrableSchedule(project) && isRegistrableUser(user, project);
    }

    private boolean isRegistrableSchedule(TeamBuildingProject project) {
        return project.getCurrentSchedule()
                .map(ProjectSchedule::isIdeaRegistrable)
                .orElse(false);
    }

    private boolean isRegistrableUser(User user, TeamBuildingProject project) {
        return user.getIdeas().stream()
                .filter(idea -> idea.getProject() == project)
                .noneMatch(Idea::isRegistered);
    }

    private boolean isCanEnroll(TeamBuildingProject project, User user) {
        return isCanEnrollSchedule(project) && isCanEnrollUser(user, project);
    }

    private boolean isCanEnrollSchedule(TeamBuildingProject project) {
        return project.getCurrentSchedule()
                .map(ProjectSchedule::isEnrollmentAvailable)
                .orElse(false);
    }

    private boolean isCanEnrollUser(User user, TeamBuildingProject project) {
        return !user.isMemberOf(project);
    }
}
