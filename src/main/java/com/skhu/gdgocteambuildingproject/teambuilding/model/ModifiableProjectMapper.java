package com.skhu.gdgocteambuildingproject.teambuilding.model;

import com.skhu.gdgocteambuildingproject.admin.dto.project.ModifiableProjectResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModifiableProjectMapper {
    private final ProjectAvailablePartMapper availablePartMapper;
    private final ProjectScheduleMapper scheduleMapper;

    public ModifiableProjectResponseDto map(TeamBuildingProject project) {
        List<Long> participantUserIds = getParticipatedUserIds(project);

        return ModifiableProjectResponseDto.builder()
                .projectId(project.getId())
                .projectName(project.getName())
                .maxMemberCount(project.getMaxMemberCount())
                .availableParts(availablePartMapper.map(project))
                .schedules(scheduleMapper.map(project.getSchedules()))
                .participatedUserIds(participantUserIds)
                .build();
    }

    private List<Long> getParticipatedUserIds(TeamBuildingProject project) {
        return project.getParticipants().stream()
                .map(participant -> participant.getUser().getId())
                .toList();
    }
}
