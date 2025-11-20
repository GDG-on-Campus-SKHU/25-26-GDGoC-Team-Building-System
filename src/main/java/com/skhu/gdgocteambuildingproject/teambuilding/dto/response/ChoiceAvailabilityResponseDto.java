package com.skhu.gdgocteambuildingproject.teambuilding.dto.response;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.Choice;
import lombok.Builder;

@Builder
public record ChoiceAvailabilityResponseDto(
        Choice choice,
        boolean available
) {
}
