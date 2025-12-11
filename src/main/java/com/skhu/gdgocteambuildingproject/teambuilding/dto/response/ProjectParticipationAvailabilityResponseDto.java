package com.skhu.gdgocteambuildingproject.teambuilding.dto.response;

import lombok.Builder;

@Builder
public record ProjectParticipationAvailabilityResponseDto(
        boolean available
) {
}

