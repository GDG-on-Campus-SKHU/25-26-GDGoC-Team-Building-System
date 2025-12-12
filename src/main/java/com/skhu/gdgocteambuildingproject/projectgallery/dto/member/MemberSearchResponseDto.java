package com.skhu.gdgocteambuildingproject.projectgallery.dto.member;

import lombok.Builder;

@Builder
public record MemberSearchResponseDto(
        Long userId,
        String name,
        String school,
        String generationAndPosition,
        boolean isSelected
) {
}
