package com.skhu.gdgocteambuildingproject.teambuilding.model;

import static com.skhu.gdgocteambuildingproject.Idea.domain.enumtype.EnrollmentStatus.ACCEPTED;
import static com.skhu.gdgocteambuildingproject.Idea.domain.enumtype.EnrollmentStatus.SCHEDULED_TO_ACCEPT;

import com.skhu.gdgocteambuildingproject.Idea.domain.Idea;
import com.skhu.gdgocteambuildingproject.Idea.domain.IdeaEnrollment;
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
            List<RosterMemberResponseDto> members = idea.getEnrollments().stream()
                    .filter(enrollment -> enrollment.getPart() == part)
                    .filter(this::isAccepted)
                    .map(rosterMemberMapper::map)
                    .toList();

            rosters.add(RosterPartResponseDto.builder()
                    .part(part)
                    .currentMemberCount(idea.getCurrentMemberCountIncludeNotConfirm(part))
                    .maxMemberCount(idea.getMaxMemberCountOf(part))
                    .members(members)
                    .build());
        }

        return Collections.unmodifiableList(rosters);
    }

    private boolean isAccepted(IdeaEnrollment enrollment) {
        return enrollment.getStatus() == ACCEPTED || enrollment.getStatus() == SCHEDULED_TO_ACCEPT;
    }
}

