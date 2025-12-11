package com.skhu.gdgocteambuildingproject.teambuilding.model;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.Idea;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.IdeaMember;
import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.RosterResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RosterMapper {

    private final RosterPartMapper rosterPartMapper;

    public RosterResponseDto map(User user, Idea idea) {
        IdeaMember member = getMember(user, idea);

        return RosterResponseDto.builder()
                .ideaId(idea.getId())
                .ideaTitle(idea.getTitle())
                .ideaIntroduction(idea.getIntroduction())
                .myRole(member.getRole())
                .rosters(rosterPartMapper.map(idea))
                .build();
    }

    private IdeaMember getMember(User user, Idea idea) {
        return idea.getMembers().stream()
                .filter(member -> user.equals(member.getUser()))
                .findAny()
                .orElseThrow(() -> new IllegalStateException(ExceptionMessage.NOT_MEMBER_OF_IDEA.getMessage()));
    }
}

