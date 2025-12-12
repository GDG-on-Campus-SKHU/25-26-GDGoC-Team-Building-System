package com.skhu.gdgocteambuildingproject.teambuilding.model.mapper;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectTopic;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.project.ModifiableProjectResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModifiableProjectMapper {
    private final ProjectAvailablePartMapper availablePartMapper;
    private final ProjectScheduleMapper scheduleMapper;
    private final ProjectParticipantMapper participantMapper;

    public ModifiableProjectResponseDto map(TeamBuildingProject project) {
        return ModifiableProjectResponseDto.builder()
                .projectId(project.getId())
                .projectName(project.getName())
                .maxMemberCount(project.getMaxMemberCount())
                .topics(mapTopics(project.getTopics()))
                .availableParts(availablePartMapper.map(project))
                .schedules(scheduleMapper.map(project.getSchedules()))
                .participants(participantMapper.map(project.getParticipants()))
                .build();
    }

    private List<String> mapTopics(List<ProjectTopic> topics) {
        return topics.stream()
                .map(ProjectTopic::getTopic)
                .toList();
    }
}
