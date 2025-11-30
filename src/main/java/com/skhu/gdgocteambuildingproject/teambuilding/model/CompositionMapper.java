package com.skhu.gdgocteambuildingproject.teambuilding.model;

import com.skhu.gdgocteambuildingproject.Idea.domain.Idea;
import com.skhu.gdgocteambuildingproject.Idea.domain.IdeaMember;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.CompositionMemberResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.CompositionPartResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.CompositionResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CompositionMapper {
    public CompositionResponseDto map(User user, Idea idea) {
        IdeaMember member = getMember(user, idea);

        return CompositionResponseDto.builder()
                .ideaId(idea.getId())
                .ideaTitle(idea.getTitle())
                .ideaIntroduction(idea.getIntroduction())
                .myRole(member.getRole())
                .compositions(mapCompositions(idea))
                .build();
    }

    private IdeaMember getMember(User user, Idea idea) {
        return idea.getMembers().stream()
                .filter(member -> user.equals(member.getUser()))
                .findAny()
                .orElseThrow(() -> new IllegalStateException(ExceptionMessage.NOT_MEMBER_OF_IDEA.getMessage()));
    }

    private List<CompositionPartResponseDto> mapCompositions(Idea idea) {
        List<CompositionPartResponseDto> compositions = new ArrayList<>();

        for (Part part : Part.values()) {
            compositions.add(CompositionPartResponseDto.builder()
                    .part(part)
                    .currentMemberCount(idea.getCurrentMemberCountOf(part))
                    .maxMemberCount(idea.getMaxMemberCountOf(part))
                    .members(mapMembers(idea.getMembersOf(part)))
                    .build());
        }

        return Collections.unmodifiableList(compositions);
    }

    private List<CompositionMemberResponseDto> mapMembers(List<IdeaMember> members) {
        return members.stream()
                .map(CompositionMemberResponseDto::from)
                .toList();
    }
}
