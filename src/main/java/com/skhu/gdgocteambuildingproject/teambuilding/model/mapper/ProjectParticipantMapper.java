package com.skhu.gdgocteambuildingproject.teambuilding.model.mapper;

import com.skhu.gdgocteambuildingproject.teambuilding.dto.project.ProjectParticipantResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectParticipant;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.domain.UserGeneration;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.Generation;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class ProjectParticipantMapper {

    public List<ProjectParticipantResponseDto> map(List<ProjectParticipant> participants) {
        return participants.stream()
                .map(this::mapParticipant)
                .toList();
    }

    private ProjectParticipantResponseDto mapParticipant(ProjectParticipant participant) {
        User user = participant.getUser();
        String generation = findLatestGeneration(user);

        return ProjectParticipantResponseDto.builder()
                .participantId(participant.getId())
                .userId(user.getId())
                .name(user.getName())
                .school(user.getSchool())
                .generation(generation)
                .part(user.getPart())
                .build();
    }

    private String findLatestGeneration(User user) {
        Optional<Generation> latestGeneration = user.getGeneration().stream()
                .map(UserGeneration::getGeneration)
                .max(Comparator.comparing(Generation::ordinal));

        return latestGeneration
                .map(Generation::getLabel)
                .orElse(null);
    }
}

