package com.skhu.gdgocteambuildingproject.teambuilding.model;

import com.skhu.gdgocteambuildingproject.Idea.domain.Idea;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.IdeaCreatorInfoResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.IdeaDetailInfoResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.IdeaMemberCompositionResponseDto;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class IdeaDetailInfoMapper {
    private final IdeaCreatorInfoMapper creatorInfoMapper;
    private final IdeaMemberCompositionMapper compositionMapper;

    public IdeaDetailInfoResponseDto map(Idea idea) {
        IdeaCreatorInfoResponseDto creator = creatorInfoMapper.map(idea);
        List<IdeaMemberCompositionResponseDto> compositions = compositionMapper.map(idea);

        return IdeaDetailInfoResponseDto.builder()
                .ideaId(idea.getId())
                .title(idea.getTitle())
                .introduction(idea.getIntroduction())
                .description(idea.getDescription())
                .creator(creator)
                .compositions(compositions)
                .build();
    }
}
