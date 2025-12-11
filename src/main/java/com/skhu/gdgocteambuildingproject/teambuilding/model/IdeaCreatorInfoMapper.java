package com.skhu.gdgocteambuildingproject.teambuilding.model;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.Idea;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.IdeaCreatorInfoResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class IdeaCreatorInfoMapper {
    public IdeaCreatorInfoResponseDto map(Idea idea) {
        User creator = idea.getCreator();

        return IdeaCreatorInfoResponseDto.builder()
                .creatorName(creator.getName())
                // 회원가입시 파트와 Idea에 등록한 파트가 다를 수 있기 때문에 Idea를 통해 조회
                .part(idea.getCreatorPart())
                .school(creator.getSchool())
                .build();
    }
}
