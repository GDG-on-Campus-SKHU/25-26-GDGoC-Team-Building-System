package com.skhu.gdgocteambuildingproject.teambuilding.model;

import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.NOT_PARTICIPATED;

import com.skhu.gdgocteambuildingproject.teambuilding.repository.ProjectParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ParticipationUtil {

    private final ProjectParticipantRepository participantRepository;

    public boolean isParticipated(long userId, long projectId) {
        return participantRepository.existsByUserIdAndProjectId(userId, projectId);
    }

    public void validateParticipation(long userId, long projectId) {
        boolean participated = participantRepository.existsByUserIdAndProjectId(userId, projectId);

        if (!participated) {
            throw new IllegalStateException(NOT_PARTICIPATED.getMessage());
        }
    }
}
