package com.skhu.gdgocteambuildingproject.admin.dto.project;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import lombok.Builder;

@Builder
public record ProjectParticipantResponseDto(
        long participantId,
        String name,
        String school,
        String generation,
        Part part
) {
}
