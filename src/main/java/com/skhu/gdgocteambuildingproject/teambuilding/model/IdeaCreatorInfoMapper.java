package com.skhu.gdgocteambuildingproject.teambuilding.model;

import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.IdeaCreatorInfoResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class IdeaCreatorInfoMapper {
    public IdeaCreatorInfoResponseDto map(User creator) {
        return IdeaCreatorInfoResponseDto.builder()
                .creatorName(creator.getName())
                .part(creator.getPart())
                .school(creator.getSchool())
                .build();
    }
}
