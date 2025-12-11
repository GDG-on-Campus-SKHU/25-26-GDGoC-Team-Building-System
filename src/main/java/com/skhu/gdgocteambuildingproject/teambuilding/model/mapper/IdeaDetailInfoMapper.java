package com.skhu.gdgocteambuildingproject.teambuilding.model.mapper;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.Idea;
import com.skhu.gdgocteambuildingproject.admin.dto.idea.AdminIdeaDetailResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.IdeaCreatorInfoResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.IdeaDetailInfoResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.IdeaMemberCompositionResponseDto;
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
                .topicId(idea.getTopic().getId())
                .topic(idea.getTopic().getTopic())
                .creator(creator)
                .compositions(compositions)
                .build();
    }

    public AdminIdeaDetailResponseDto mapForAdmin(Idea idea) {
        IdeaCreatorInfoResponseDto creator = creatorInfoMapper.map(idea);
        List<IdeaMemberCompositionResponseDto> compositions = compositionMapper.map(idea);

        return AdminIdeaDetailResponseDto.builder()
                .ideaId(idea.getId())
                .title(idea.getTitle())
                .introduction(idea.getIntroduction())
                .description(idea.getDescription())
                .topicId(idea.getTopic().getId())
                .topic(idea.getTopic().getTopic())
                .creator(creator)
                .compositions(compositions)
                .deleted(idea.isDeleted())
                .build();
    }
}
