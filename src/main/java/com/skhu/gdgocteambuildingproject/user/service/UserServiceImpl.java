package com.skhu.gdgocteambuildingproject.user.service;

import com.skhu.gdgocteambuildingproject.global.util.PrincipalUtil;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.teambuilding.model.ProjectUtil;
import com.skhu.gdgocteambuildingproject.teambuilding.repository.ProjectParticipantRepository;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.dto.UserIdResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.PROJECT_NOT_EXIST;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final ProjectUtil projectUtil;
    private final ProjectParticipantRepository projectParticipantRepository;

    @Override
    public UserIdResponseDto getUserIdFromToken(Principal principal) {
        return UserIdResponseDto.builder()
                .userId(PrincipalUtil.getUserIdFrom(principal))
                .build();
    }

    @Override
    public boolean isUserParticipated(User user) {
        TeamBuildingProject currentProject = projectUtil.findCurrentProject()
                .orElseThrow(() -> new EntityNotFoundException(PROJECT_NOT_EXIST.getMessage()));

        return projectParticipantRepository.existsByUserIdAndProjectId(user.getId(), currentProject.getId());
    }
}
