package com.skhu.gdgocteambuildingproject.teambuilding.model.mapper;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.Idea;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.IdeaEnrollment;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.RosterMemberResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.RosterPartResponseDto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RosterPartMapper {

    private final RosterMemberMapper rosterMemberMapper;

    public List<RosterPartResponseDto> map(Idea idea) {
        List<RosterPartResponseDto> rosters = new ArrayList<>();

        for (Part part : Part.values()) {
            Stream<RosterMemberResponseDto> confirmedMembers = mapConfirmedMembers(idea, part);
            Stream<RosterMemberResponseDto> notConfirmedMembers = mapNotConfirmedMembers(idea, part);

            List<RosterMemberResponseDto> allMembers = Stream.concat(confirmedMembers, notConfirmedMembers).toList();

            rosters.add(RosterPartResponseDto.builder()
                    .part(part)
                    .currentMemberCount(idea.getCurrentMemberCountIncludeNotConfirm(part))
                    .maxMemberCount(idea.getMaxMemberCountOf(part))
                    .members(allMembers)
                    .build());
        }

        return Collections.unmodifiableList(rosters);
    }

    /**
     * 멤버를 DTO로 매핑한다.
     */
    private Stream<RosterMemberResponseDto> mapConfirmedMembers(Idea idea, Part part) {
        return idea.getMembers().stream()
                .filter(member -> member.getPart() == part)
                .map(rosterMemberMapper::map);
    }

    /**
     * 수락 예정인 지원을 DTO로 매핑한다.
     */
    private Stream<RosterMemberResponseDto> mapNotConfirmedMembers(Idea idea, Part part) {
        return idea.getEnrollments().stream()
                .filter(enrollment -> enrollment.getPart() == part)
                .filter(IdeaEnrollment::isScheduledToAccept)
                .map(rosterMemberMapper::map);
    }
}

