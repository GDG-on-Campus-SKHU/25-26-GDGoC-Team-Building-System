package com.skhu.gdgocteambuildingproject.teambuilding.model;

import com.skhu.gdgocteambuildingproject.Idea.domain.Idea;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.IdeaTitleInfoResponseDto;
import org.springframework.stereotype.Component;

@Component
public class IdeaTitleInfoMapper {
    public IdeaTitleInfoResponseDto map(Idea idea) {
        return IdeaTitleInfoResponseDto.builder()
                .ideaId(idea.getId())
                .title(idea.getTitle())
                .introduction(idea.getIntroduction())
                .currentMemberCount(idea.getCurrentMemberCount())
                .maxMemberCount(idea.getMaxMemberCount())
                .build();
    }
}
