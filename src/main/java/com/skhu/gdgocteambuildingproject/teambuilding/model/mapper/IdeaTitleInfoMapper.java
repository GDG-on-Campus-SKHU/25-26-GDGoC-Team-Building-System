package com.skhu.gdgocteambuildingproject.teambuilding.model.mapper;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.Idea;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.IdeaTitleInfoIncludeDeletedResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.IdeaTitleInfoResponseDto;
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

    public IdeaTitleInfoIncludeDeletedResponseDto mapIncludeDeleted(Idea idea) {
        return IdeaTitleInfoIncludeDeletedResponseDto.builder()
                .ideaId(idea.getId())
                .title(idea.getTitle())
                .introduction(idea.getIntroduction())
                .creatorName(idea.getCreator().getName())
                .currentMemberCount(idea.getCurrentMemberCount())
                .maxMemberCount(idea.getMaxMemberCount())
                .deleted(idea.isDeleted())
                .build();
    }
}
