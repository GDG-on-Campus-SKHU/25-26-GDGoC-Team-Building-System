package com.skhu.gdgocteambuildingproject.teambuilding.dto.response;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import java.util.List;
import lombok.Builder;

@Builder
public record CompositionPartResponseDto(
        Part part,
        int currentMemberCount,
        int maxMemberCount,
        List<CompositionMemberResponseDto> members
) {
}
