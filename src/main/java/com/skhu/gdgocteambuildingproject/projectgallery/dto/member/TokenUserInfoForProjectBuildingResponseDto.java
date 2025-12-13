package com.skhu.gdgocteambuildingproject.projectgallery.dto.member;

import lombok.Builder;

@Builder
public record TokenUserInfoForProjectBuildingResponseDto(
        Long userId,
        String name,
        String school,
        String generationAndPosition
) {
}
