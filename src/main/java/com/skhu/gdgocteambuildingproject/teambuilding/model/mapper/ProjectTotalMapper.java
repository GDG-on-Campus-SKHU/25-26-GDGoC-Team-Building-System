package com.skhu.gdgocteambuildingproject.teambuilding.model.mapper;

import com.skhu.gdgocteambuildingproject.teambuilding.dto.project.ProjectTotalResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.project.ProjectTopicResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectTopic;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectTotalMapper {
    private final ProjectAvailablePartMapper availablePartMapper;
    private final ProjectScheduleMapper scheduleMapper;

    public ProjectTotalResponseDto map(TeamBuildingProject project) {
        return ProjectTotalResponseDto.builder()
                .projectId(project.getId())
                .projectName(project.getName())
                .maxMemberCount(project.getMaxMemberCount())
                .topics(mapTopics(project.getTopics()))
                .availableParts(availablePartMapper.map(project))
                .schedules(scheduleMapper.map(project.getSchedules()))
                .build();
    }

    private List<ProjectTopicResponseDto> mapTopics(List<ProjectTopic> topics) {
        return topics.stream()
                .map(topic -> ProjectTopicResponseDto.builder()
                        .topicId(topic.getId())
                        .topic(topic.getTopic())
                        .build())
                .toList();
    }
}

