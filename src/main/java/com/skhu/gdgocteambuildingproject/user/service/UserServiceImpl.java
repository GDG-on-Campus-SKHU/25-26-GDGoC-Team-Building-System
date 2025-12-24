package com.skhu.gdgocteambuildingproject.user.service;

import com.skhu.gdgocteambuildingproject.global.util.PrincipalUtil;
import com.skhu.gdgocteambuildingproject.teambuilding.model.ProjectUtil;
import com.skhu.gdgocteambuildingproject.teambuilding.repository.ProjectParticipantRepository;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.dto.UserIdResponseDto;
import java.security.Principal;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return projectUtil.findCurrentProject()
                .map(currentProject -> projectParticipantRepository.existsByUserIdAndProjectId(user.getId(), currentProject.getId()))
                .orElse(false);
    }
}
