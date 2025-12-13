package com.skhu.gdgocteambuildingproject.teambuilding.model.mapper;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.Idea;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.TemporaryIdeaDetailResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TemporaryIdeaMapper {

    private final IdeaDetailInfoMapper ideaMapper;

    public TemporaryIdeaDetailResponseDto map(Idea idea) {
        return TemporaryIdeaDetailResponseDto.builder()
                .lastTemporarySavedAt(idea.getLastTemporarySavedAt())
                .idea(ideaMapper.map(idea))
                .build();
    }
}
