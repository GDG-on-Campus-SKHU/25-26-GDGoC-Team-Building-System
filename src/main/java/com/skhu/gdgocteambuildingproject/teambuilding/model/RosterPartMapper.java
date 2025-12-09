package com.skhu.gdgocteambuildingproject.teambuilding.model;

import com.skhu.gdgocteambuildingproject.Idea.domain.Idea;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.RosterMemberResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.RosterPartResponseDto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RosterPartMapper {

    private final RosterMemberMapper rosterMemberMapper;

    public List<RosterPartResponseDto> map(Idea idea) {
        List<RosterPartResponseDto> rosters = new ArrayList<>();

        for (Part part : Part.values()) {
            List<RosterMemberResponseDto> members = idea.getMembers().stream()
                    .map(rosterMemberMapper::map)
                    .toList();

            rosters.add(RosterPartResponseDto.builder()
                    .part(part)
                    .currentMemberCount(idea.getCurrentMemberCountOf(part))
                    .maxMemberCount(idea.getMaxMemberCountOf(part))
                    .members(members)
                    .build());
        }

        return Collections.unmodifiableList(rosters);
    }
}

