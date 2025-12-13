package com.skhu.gdgocteambuildingproject.teambuilding.model;

import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.NOT_PARTICIPATED;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.USER_NOT_EXIST;

import com.skhu.gdgocteambuildingproject.teambuilding.repository.ProjectParticipantRepository;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserRole;
import com.skhu.gdgocteambuildingproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ParticipationUtil {

    private final ProjectParticipantRepository participantRepository;
    private final UserRepository userRepository;

    public boolean isParticipated(long userId, long projectId) {
        return participantRepository.existsByUserIdAndProjectId(userId, projectId);
    }

    public void validateParticipation(long userId, long projectId) {
        if (isAdmin(userId)) {
            return;
        }

        boolean participated = participantRepository.existsByUserIdAndProjectId(userId, projectId);

        if (!participated) {
            throw new IllegalStateException(NOT_PARTICIPATED.getMessage());
        }
    }

    private boolean isAdmin(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException(USER_NOT_EXIST.getMessage()));

        return user.getRole() == UserRole.ROLE_SKHU_ADMIN;
    }
}
