package com.skhu.gdgocteambuildingproject.projectgallery.dto.member;

import lombok.Builder;

@Builder
public record MemberSearchResponseDto(
        String name,
        String school,
        String generationAndPosition,
        boolean isSelected
) {
}
