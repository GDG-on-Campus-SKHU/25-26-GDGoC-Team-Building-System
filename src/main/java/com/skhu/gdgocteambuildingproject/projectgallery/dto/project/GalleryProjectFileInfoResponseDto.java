package com.skhu.gdgocteambuildingproject.projectgallery.dto.project;

import lombok.Builder;

@Builder
public record GalleryProjectFileInfoResponseDto(
        String fileUrl
) {
}
