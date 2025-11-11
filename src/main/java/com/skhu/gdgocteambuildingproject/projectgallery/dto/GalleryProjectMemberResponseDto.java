package com.skhu.gdgocteambuildingproject.projectgallery.dto;

import lombok.Builder;

@Builder
public record GalleryProjectMemberResponseDto(
        String memberRole,
        String name,
        String part
) {
}
