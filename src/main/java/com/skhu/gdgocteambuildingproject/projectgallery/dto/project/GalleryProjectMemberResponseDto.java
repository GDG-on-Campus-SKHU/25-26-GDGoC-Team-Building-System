package com.skhu.gdgocteambuildingproject.projectgallery.dto.project;

import lombok.Builder;

@Builder
public record GalleryProjectMemberResponseDto(
        String memberRole,
        String name,
        String part
) {
}
