package com.skhu.gdgocteambuildingproject.teambuilding.dto.idea;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import java.util.List;
import lombok.Builder;

@Builder
public record RosterPartResponseDto(
        Part part,
        int currentMemberCount,
        int maxMemberCount,
        List<RosterMemberResponseDto> members
) {
}
