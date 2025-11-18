package com.skhu.gdgocteambuildingproject.projectgallery.dto.project.info;

import lombok.Builder;

@Builder
public record GalleryProjectFileInfoResponseDto(
        String fileUrl
) {
}
