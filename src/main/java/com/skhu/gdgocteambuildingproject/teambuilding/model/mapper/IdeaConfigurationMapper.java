package com.skhu.gdgocteambuildingproject.teambuilding.model.mapper;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectTopic;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.IdeaConfigurationResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.project.ProjectTopicResponseDto;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class IdeaConfigurationMapper {

    public IdeaConfigurationResponseDto map(TeamBuildingProject project) {
        return IdeaConfigurationResponseDto.builder()
                .topics(mapTopics(project.getTopics()))
                .availableParts(project.getAvailableParts())
                .maxMemberCount(project.getMaxMemberCount())
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

